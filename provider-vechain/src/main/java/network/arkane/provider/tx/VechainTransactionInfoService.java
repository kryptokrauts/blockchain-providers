package network.arkane.provider.tx;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.blockchain.Block;
import network.arkane.provider.core.model.blockchain.Event;
import network.arkane.provider.core.model.blockchain.Receipt;
import network.arkane.provider.core.model.blockchain.Transaction;
import network.arkane.provider.core.model.blockchain.Transfer;
import network.arkane.provider.gateway.VechainGateway;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class VechainTransactionInfoService implements TransactionInfoService, DisposableBean {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder().setNameFormat("vechain-tx-info-%d").build());
    ;
    private final VechainGateway vechainGateway;
    private final HasReachedFinalityService hasReachedFinalityService;

    public VechainTransactionInfoService(final VechainGateway vechainGateway, final HasReachedFinalityService hasReachedFinalityService) {
        this.vechainGateway = vechainGateway;
        this.hasReachedFinalityService = hasReachedFinalityService;
    }

    public SecretType type() {
        return SecretType.VECHAIN;
    }

    @Override
    @SneakyThrows
    public TxInfo getTransaction(String hash) {
        CompletableFuture<Transaction> transactionFuture = CompletableFuture.supplyAsync(() -> vechainGateway.getTransaction(hash), executorService);
        CompletableFuture<Receipt> receiptFuture = CompletableFuture.supplyAsync(() -> vechainGateway.getTransactionReceipt(hash), executorService);
        CompletableFuture<Block> blockFuture = CompletableFuture.supplyAsync(() -> vechainGateway.getBlock(), executorService);
        Transaction transaction = transactionFuture.get();
        if (transaction == null) {
            return mapToUnfound(hash);
        } else {
            Receipt receipt = receiptFuture.get();
            if (receipt == null) {
                return mapToUnfound(hash);
            } else {
                return mapToMined(transaction, receipt, blockFuture.get());
            }
        }
    }

    private TxInfo mapToMined(Transaction transaction,
                              Receipt receipt,
                              Block currentBlock) {


        final BigInteger confirmations = new BigInteger(currentBlock.getNumber()).subtract(BigInteger.valueOf(transaction.getMeta().getBlockNumber())).add(BigInteger.ONE);
        return VechainTxInfo.vechainTxInfoBuilder()
                            .hash(transaction.getId())
                            .blockHash(transaction.getMeta().getBlockID())
                            .blockNumber(BigInteger.valueOf(transaction.getMeta().getBlockNumber()))
                            .confirmations(confirmations)
                            .hasReachedFinality(hasReachedFinalityService.hasReachedFinality(SecretType.VECHAIN, confirmations))
                            .status(receipt.isReverted() ? TxStatus.FAILED : TxStatus.SUCCEEDED)
                            .nonce(transaction.getNonce())
                            .gas(BigInteger.valueOf(transaction.getGas()))
                            .gasUsed(BigInteger.valueOf(receipt.getGasUsed()))
                            .gasPriceCoef(BigInteger.valueOf(transaction.getGasPriceCoef()))
                            .outputs(mapOutputs(receipt))
                            .build();
    }

    private List<VechainReceiptOutput> mapOutputs(Receipt receipt) {

        if (receipt.getOutputs() != null) {
            return receipt.getOutputs().stream()
                          .map(o -> VechainReceiptOutput.builder()
                                                        .contractAddress(o.getContractAddress())
                                                        .events(mapEvents(o.getEvents()))
                                                        .transfers(mapTransfers(o.getTransfers()))
                                                        .build()
                              )
                          .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<VechainTxTransfer> mapTransfers(ArrayList<Transfer> transfers) {
        if (transfers != null) {
            return transfers.stream()
                            .map(t -> VechainTxTransfer.builder()
                                                       .amount(t.getAmount())
                                                       .recipient(t.getRecipient())
                                                       .sender(t.getSender())
                                                       .build())
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<VechainTxEvent> mapEvents(ArrayList<Event> events) {
        if (events != null) {
            return events
                    .stream()
                    .map(e -> VechainTxEvent.builder()
                                            .address(e.getAddress())
                                            .data(e.getData())
                                            .topics(e.getTopics() == null ? Collections.emptyList() : e.getTopics())
                                            .build()
                        )
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private TxInfo mapToUnfound(String hash) {
        return TxInfo.builder()
                     .hash(hash)
                     .confirmations(BigInteger.ZERO)
                     .status(TxStatus.UNKNOWN)
                     .build();
    }

    public void destroy() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

}
