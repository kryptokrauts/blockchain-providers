package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.MaticWeb3JGateway;
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
public class MaticTransactionInfoService implements TransactionInfoService {


    private MaticWeb3JGateway maticWeb3jGateway;

    public MaticTransactionInfoService(MaticWeb3JGateway maticWeb3jGateway) {
        this.maticWeb3jGateway = maticWeb3jGateway;
    }

    public SecretType type() {
        return SecretType.MATIC;
    }

    @Override
    public MaticTxInfo getTransaction(String hash) {
        try {
            return maticWeb3jGateway.web3().ethGetTransactionByHash(hash)
                                    .send()
                                    .getTransaction().map(tx -> {
                        try {
                            return mapToTxInfo(tx);
                        } catch (IOException e) {
                            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
                        }
                    }).orElseGet(() -> MaticTxInfo.ethereumTxInfoBuilder().hash(hash).status(TxStatus.UNKNOWN).build());

        } catch (IOException e) {
            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
        }
    }

    private MaticTxInfo mapToTxInfo(Transaction tx) throws IOException {
        CompletableFuture<EthGetTransactionReceipt> receiptFuture = maticWeb3jGateway.web3().ethGetTransactionReceipt(tx.getHash()).sendAsync();
        CompletableFuture<EthBlockNumber> blockNumberFuture = maticWeb3jGateway.web3().ethBlockNumber().sendAsync();
        try {
            EthBlockNumber ethBlockNumber = blockNumberFuture.get();
            return receiptFuture.get().getTransactionReceipt()
                                .map((r) -> mapToMinedTxInfo(tx, r, ethBlockNumber))
                                .orElseGet(() -> mapToUnminedTxInfo(tx));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private MaticTxInfo mapToUnminedTxInfo(Transaction tx) {
        return MaticTxInfo.ethereumTxInfoBuilder()
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

    private MaticTxInfo mapToMinedTxInfo(Transaction tx,
                                         TransactionReceipt receipt,
                                         EthBlockNumber blockNumber) {
        return MaticTxInfo.ethereumTxInfoBuilder()
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

    private List<MaticTxLog> mapLogs(TransactionReceipt tx) {
        return tx.getLogs() == null ? new ArrayList<>() : tx.getLogs().stream()
                                                            .map(l -> MaticTxLog.builder()
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
