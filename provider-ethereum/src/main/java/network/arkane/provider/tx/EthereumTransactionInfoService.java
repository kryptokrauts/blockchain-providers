package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class EthereumTransactionInfoService implements TransactionInfoService {


    private Web3j defaultWeb3j;

    public EthereumTransactionInfoService(@Qualifier("ethereumWeb3j") Web3j web3j) {
        this.defaultWeb3j = web3j;
    }

    public SecretType type() {
        return SecretType.ETHEREUM;
    }

    @Override
    public EthereumTxInfo getTransaction(String hash) {
        return getTransaction(hash, Collections.emptyMap());
    }

    @Override
    public EthereumTxInfo getTransaction(String hash,
                                         Map<String, Object> parameters) {
        try {
            Web3j web3J = getWeb3J(parameters);
            return web3J.ethGetTransactionByHash(hash)
                        .send()
                        .getTransaction().map(tx -> {
                        try {
                            return mapToTxInfo(tx, web3J);
                        } catch (IOException e) {
                            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
                        }
                    }).orElseGet(() -> EthereumTxInfo.ethereumTxInfoBuilder().hash(hash).status(TxStatus.UNKNOWN).build());

        } catch (IOException e) {
            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
        }
    }

    private Web3j getWeb3J(Map<String, Object> parameters) {
        if (parameters != null && parameters.containsKey("endpoint")) {
            return Web3j.build(new HttpService((String) parameters.get("endpoint"), false));
        } else {
            return defaultWeb3j;
        }
    }

    private EthereumTxInfo mapToTxInfo(Transaction tx,
                                       Web3j web3J) throws IOException {
        CompletableFuture<EthGetTransactionReceipt> receiptFuture = web3J.ethGetTransactionReceipt(tx.getHash()).sendAsync();
        CompletableFuture<EthBlockNumber> blockNumberFuture = web3J.ethBlockNumber().sendAsync();
        try {
            EthBlockNumber ethBlockNumber = blockNumberFuture.get();
            return receiptFuture.get().getTransactionReceipt()
                                .map((r) -> mapToMinedTxInfo(tx, r, ethBlockNumber))
                                .orElseGet(() -> mapToUnminedTxInfo(tx));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private EthereumTxInfo mapToUnminedTxInfo(Transaction tx) {
        return EthereumTxInfo.ethereumTxInfoBuilder()
                             .blockHash(tx.getBlockHash())
                             .blockNumber(tx.getBlockNumber())
                             .hash(tx.getHash())
                             .from(tx.getFrom())
                             .to(tx.getTo())
                             .status(TxStatus.PENDING)
                             .confirmations(BigInteger.ZERO)
                             .gas(tx.getGas())
                             .gasPrice(tx.getGasPrice())
                             .nonce(tx.getNonce())
                             .build();
    }

    private EthereumTxInfo mapToMinedTxInfo(Transaction tx,
                                            TransactionReceipt receipt,
                                            EthBlockNumber blockNumber) {
        return EthereumTxInfo.ethereumTxInfoBuilder()
                             .blockHash(receipt.getBlockHash())
                             .blockNumber(receipt.getBlockNumber())
                             .hash(receipt.getTransactionHash())
                             .from(receipt.getFrom())
                             .to(receipt.getTo())
                             .status(getStatus(receipt))
                             .confirmations(blockNumber.getBlockNumber().subtract(receipt.getBlockNumber()).add(BigInteger.ONE))
                             .gasUsed(receipt.getGasUsed())
                             .gas(tx.getGas())
                             .gasPrice(tx.getGasPrice())
                             .nonce(tx.getNonce())
                             .logs(mapLogs(receipt))
                             .build();
    }

    private List<EthereumTxLog> mapLogs(TransactionReceipt tx) {
        return tx.getLogs() == null ? new ArrayList<>() : tx.getLogs().stream()
                                                            .map(l -> EthereumTxLog.builder()
                                                                                   .data(l.getData())
                                                                                   .logIndex(l.getLogIndex())
                                                                                   .topics(l.getTopics())
                                                                                   .type(l.getType())
                                                                                   .build())
                                                            .collect(Collectors.toList());
    }

    private TxStatus getStatus(TransactionReceipt tx) {
        switch (tx.getStatus()) {
            case "0x0":
                return TxStatus.FAILED;
            case "0x1":
                return TxStatus.SUCCEEDED;
            default:
                return TxStatus.UNKNOWN;
        }
    }
}
