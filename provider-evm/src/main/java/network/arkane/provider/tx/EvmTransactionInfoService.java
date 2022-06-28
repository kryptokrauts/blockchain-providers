package network.arkane.provider.tx;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public abstract class EvmTransactionInfoService implements TransactionInfoService {

    private final Web3j defaultWeb3j;
    private final HasReachedFinalityService hasReachedFinalityService;

    public EvmTransactionInfoService(final Web3j web3j,
                                     final HasReachedFinalityService hasReachedFinalityService) {
        this.defaultWeb3j = web3j;
        this.hasReachedFinalityService = hasReachedFinalityService;
    }

    public abstract SecretType type();

    @Override
    public EvmTxInfo getTransaction(final String hash) {
        return getTransaction(hash, Collections.emptyMap());
    }

    @Override
    public EvmTxInfo getTransaction(final String hash,
                                    final Map<String, Object> parameters) {
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
                    }).orElseGet(() -> EvmTxInfo.evmTxInfoBuilder().hash(hash).status(TxStatus.UNKNOWN).build());

        } catch (IOException e) {
            throw ArkaneException.arkaneException().message("Error getting transaction").errorCode("transaction.exception.unknown").build();
        }
    }

    private Web3j getWeb3J(final Map<String, Object> parameters) {
        if (parameters != null && parameters.containsKey("endpoint")) {
            return Web3j.build(new HttpService((String) parameters.get("endpoint"), false));
        } else {
            return defaultWeb3j;
        }
    }

    private EvmTxInfo mapToTxInfo(final Transaction tx,
                                  final Web3j web3J) throws IOException {
        CompletableFuture<EthGetTransactionReceipt> receiptFuture = web3J.ethGetTransactionReceipt(tx.getHash()).sendAsync();
        CompletableFuture<EthBlockNumber> blockNumberFuture = web3J.ethBlockNumber().sendAsync();
        try {
            EthBlockNumber ethBlockNumber = blockNumberFuture.get();
            return receiptFuture.get().getTransactionReceipt()
                                .map((r) -> this.mapToMinedTxInfo(tx, r, ethBlockNumber, web3J))
                                .orElseGet(() -> this.mapToUnminedTxInfo(tx));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private EvmTxInfo mapToUnminedTxInfo(final Transaction tx) {
        return EvmTxInfo.evmTxInfoBuilder()
                        .value(tx.getValue())
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
                        .hasReachedFinality(false)
                        .build();
    }

    private EvmTxInfo mapToMinedTxInfo(final Transaction tx,
                                       final TransactionReceipt receipt,
                                       final EthBlockNumber blockNumber,
                                       final Web3j web3j) {
        final BigInteger confirmations = blockNumber.getBlockNumber().subtract(receipt.getBlockNumber()).add(BigInteger.ONE);
        EvmTxInfo.EvmTxInfoBuilder builder = EvmTxInfo.evmTxInfoBuilder()
                                                      .value(tx.getValue())
                                                      .blockHash(receipt.getBlockHash())
                                                      .blockNumber(receipt.getBlockNumber())
                                                      .hash(receipt.getTransactionHash())
                                                      .from(receipt.getFrom())
                                                      .to(receipt.getTo())
                                                      .status(getStatus(receipt))
                                                      .confirmations(confirmations)
                                                      .hasReachedFinality(hasReachedFinalityService.hasReachedFinality(type(), confirmations))
                                                      .gasUsed(receipt.getGasUsed())
                                                      .gas(tx.getGas())
                                                      .gasPrice(tx.getGasPrice())
                                                      .nonce(tx.getNonce())
                                                      .logs(mapLogs(receipt));
        this.getTxBlock(receipt, web3j)
            .ifPresent(ethBlock -> builder.timestamp(Instant.ofEpochSecond(ethBlock.getBlock().getTimestamp().longValue())
                                                            .atZone(ZoneOffset.UTC)
                                                            .toLocalDateTime()));
        return builder.build();
    }

    private Optional<EthBlock> getTxBlock(final TransactionReceipt receipt,
                                          final Web3j web3j) {
        try {
            final EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(receipt.getBlockNumber()), false)
                                           .send();
            return Optional.of(ethBlock);
        } catch (IOException e) {
            log.error("Error getting EthBlock for chain: {} and blocNumber: {}", type(), receipt.getBlockNumber(), e);
            return Optional.empty();
        }
    }

    private List<EvmTxLog> mapLogs(final TransactionReceipt tx) {
        return tx.getLogs() == null ? new ArrayList<>() : tx.getLogs().stream()
                                                            .map(l -> EvmTxLog.builder()
                                                                              .data(l.getData())
                                                                              .logIndex(l.getLogIndex())
                                                                              .topics(l.getTopics())
                                                                              .type(l.getType())
                                                                              .build())
                                                            .collect(Collectors.toList());
    }

    private TxStatus getStatus(final TransactionReceipt tx) {
        return switch (tx.getStatus()) {
            case "0x0" -> TxStatus.FAILED;
            case "0x1" -> TxStatus.SUCCEEDED;
            default -> TxStatus.UNKNOWN;
        };
    }
}
