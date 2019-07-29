package network.arkane.provider.tron.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tron.grpc.GrpcClient;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxStatus;
import org.springframework.stereotype.Component;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TronTransactionInfoService implements TransactionInfoService {

    private GrpcClient grpcClient;

    public TronTransactionInfoService(GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    public SecretType type() {
        return SecretType.TRON;
    }

    @Override
    public TronTxInfo getTransaction(String hash) {
        return grpcClient.getTransactionInfoById(hash).map(transactionInfo -> createInfo(hash, transactionInfo)).orElse(null);
    }

    private TronTxInfo createInfo(String hash, Protocol.TransactionInfo transactionInfo) {
        return TronTxInfo.tronTxInfoBuilder()
                         .hash(hash)
                         .status(getStatus(transactionInfo))
                         //                          .from(transactionInfo.get)
                         //                          .to()
                         .confirmations(getConfirmations(transactionInfo))
                         .blockNumber(BigInteger.valueOf(transactionInfo.getBlockNumber()))
                         .assetIssueID(transactionInfo.getAssetIssueID())
                         .contractAddress(transactionInfo.getContractAddress() == null
                                          ? null
                                          : ByteArray.toHexString(transactionInfo.getContractAddress().toByteArray()))
                         .contractResult(getContractResults(transactionInfo))
                         .exchangeId(transactionInfo.getExchangeId())
                         .exchangeInjectAnotherAmount(transactionInfo.getExchangeInjectAnotherAmount())
                         .exchangeReceivedAmount(transactionInfo.getExchangeReceivedAmount())
                         .exchangeWithdrawAnotherAmount(transactionInfo.getExchangeWithdrawAnotherAmount())
                         .fee(transactionInfo.getFee())
                         .id(transactionInfo.getId() == null
                             ? null
                             : ByteArray.toHexString(transactionInfo.getId().toByteArray()))
                         .internalTransactions(mapInternalTransactions(transactionInfo))
                         .log(mapToLog(transactionInfo))
                         .resMessage(transactionInfo.getResMessage() == null
                                     ? null
                                     : ByteArray.toHexString(transactionInfo.getResMessage().toByteArray()))
                         .withdrawAmount(transactionInfo.getWithdrawAmount())
                         .unfreezeAmount(transactionInfo.getUnfreezeAmount())
                         .receipt(transactionInfo.getReceipt() == null
                                  ? null
                                  : mapReceipt(transactionInfo.getReceipt()))
                         .build();
    }

    private TronReceipt mapReceipt(Protocol.ResourceReceipt receipt) {
        return TronReceipt.builder()
                          .energyFee(receipt.getEnergyFee())
                          .energyUsage(receipt.getEnergyUsage())
                          .energyUsageTotal(receipt.getEnergyUsageTotal())
                          .originEnergyUsage(receipt.getOriginEnergyUsage())
                          .netFee(receipt.getNetFee())
                          .netUsage(receipt.getNetUsage())
                          .result(receipt.getResult().name())
                          .build();
    }

    private List<TronLog> mapToLog(Protocol.TransactionInfo transactionInfo) {
        return transactionInfo.getLogList() == null
               ? null
               : transactionInfo.getLogList()
                                .stream()
                                .map(l -> TronLog.builder()
                                                 .address(ByteArray.toHexString(l.getAddress().toByteArray()))
                                                 .data(ByteArray.toHexString(l.getData().toByteArray()))
                                                 .topics(l.getTopicsList() == null ? null : l.getTopicsList().stream().map(t -> ByteArray.toHexString(t.toByteArray())).collect(
                                                         Collectors.toList()))
                                                 .build())
                                .collect(Collectors.toList());
    }

    private List<TronInternalTransaction> mapInternalTransactions(Protocol.TransactionInfo transactionInfo) {
        return transactionInfo.getInternalTransactionsList() == null
               ? null
               : transactionInfo.getInternalTransactionsList().stream()
                                .map(x -> TronInternalTransaction
                                        .builder()
                                        .transferToAddress(ByteArray.toHexString(x.getTransferToAddress().toByteArray()))
                                        .callerAddress(ByteArray.toHexString(x.getCallerAddress().toByteArray()))
                                        .callValueInfo(x.getCallValueInfoList() == null
                                                       ? null
                                                       : x.getCallValueInfoList()
                                                          .stream()
                                                          .map(y -> TronCallValueInfo.builder().tokenId(y.getTokenId()).callValue(y.getCallValue()).build())
                                                          .collect(
                                                                  Collectors.toList()))
                                        .note(x.getNote() == null ? null : ByteArray.toHexString(x.getNote().toByteArray()))
                                        .rejected(x.getRejected())
                                        .build()
                                    ).collect(Collectors.toList());
    }

    private List<String> getContractResults(Protocol.TransactionInfo transactionInfo) {
        return transactionInfo.getContractResultList() == null
               ? new ArrayList<>()
               : transactionInfo.getContractResultList().stream().map(r -> ByteArray.toHexString(r.toByteArray())).collect(Collectors.toList());
    }

    private BigInteger getConfirmations(Protocol.TransactionInfo transactionInfo) {
        long currentBlock = grpcClient.getNowBlock2().getBlockHeader().getRawData().getNumber();
        return BigInteger.valueOf(currentBlock - transactionInfo.getBlockNumber() + 1);
    }

    private TxStatus getStatus(Protocol.TransactionInfo transactionInfo) {
        if (transactionInfo.getResult() != null && transactionInfo.getReceipt().getResult() != null) {
            switch (transactionInfo.getReceipt().getResult()) {
                case SUCCESS:
                    return TxStatus.SUCCEEDED;
                case DEFAULT:
                case UNKNOWN:
                case UNRECOGNIZED:
                    return TxStatus.UNKNOWN;
                default:
                    return TxStatus.FAILED;
            }
        }
        return TxStatus.UNKNOWN;
    }
}
