package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class EthereumTransactionInfoService implements TransactionInfoService {


    private EthereumWeb3JGateway ethereumWeb3JGateway;

    public EthereumTransactionInfoService(EthereumWeb3JGateway ethereumWeb3JGateway) {
        this.ethereumWeb3JGateway = ethereumWeb3JGateway;
    }

    public SecretType type() {
        return SecretType.ETHEREUM;
    }

    @Override
    public EthereumTxInfo getTransaction(String hash) {
        try {
            return ethereumWeb3JGateway.web3().ethGetTransactionByHash(hash)
                                       .send()
                                       .getTransaction().map(tx -> {
                        try {
                            return mapToTxInfo(tx);
                        } catch (IOException e) {
                            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
                        }
                    }).orElseGet(() -> EthereumTxInfo.ethereumTxInfoBuilder().hash(hash).status(TxStatus.UNKNOWN).build());

        } catch (IOException e) {
            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
        }
    }

    private EthereumTxInfo mapToTxInfo(Transaction tx) throws IOException {
        CompletableFuture<EthGetTransactionReceipt> receiptFuture = ethereumWeb3JGateway.web3().ethGetTransactionReceipt(tx.getHash()).sendAsync();
        CompletableFuture<EthBlockNumber> blockNumberFuture = ethereumWeb3JGateway.web3().ethBlockNumber().sendAsync();
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

    private EthereumTxInfo mapToMinedTxInfo(Transaction tx, TransactionReceipt receipt, EthBlockNumber blockNumber) {
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
