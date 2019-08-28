package network.arkane.provider.tron.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;

import java.math.BigInteger;
import java.util.List;

@Getter
@ToString
public class TronTxInfo extends TxInfo {

    private List<? extends TronContract> contracts;
    private String id;
    private Long fee;
    private List<String> contractResult;
    private String contractAddress;
    private TronReceipt receipt;
    private List<TronLog> log;
    private Integer result;
    private String resMessage;
    private Object assetIssueID;
    private Long withdrawAmount;
    private Long unfreezeAmount;
    private List<TronInternalTransaction> internalTransactions;
    private Long exchangeReceivedAmount;
    private Long exchangeInjectAnotherAmount;
    private Long exchangeWithdrawAnotherAmount;
    private Long exchangeId;

    protected TronTxInfo() {
    }

    @Builder(builderMethodName = "tronTxInfoBuilder")
    public TronTxInfo(String hash,
                      TxStatus status,
                      BigInteger confirmations,
                      String blockHash,
                      BigInteger blockNumber,
                      String id,
                      Long fee,
                      List<String> contractResult,
                      String contractAddress,
                      TronReceipt receipt,
                      List<TronLog> log,
                      Integer result,
                      String resMessage,
                      Object assetIssueID,
                      Long withdrawAmount,
                      Long unfreezeAmount,
                      List<TronInternalTransaction> internalTransactions,
                      Long exchangeReceivedAmount,
                      Long exchangeInjectAnotherAmount,
                      Long exchangeWithdrawAnotherAmount,
                      Long exchangeId,
                      List<? extends TronContract> contracts) {
        super(hash, status, confirmations, blockHash, blockNumber);
        this.id = id;
        this.fee = fee;
        this.contractResult = contractResult;
        this.contractAddress = contractAddress;
        this.receipt = receipt;
        this.log = log;
        this.result = result;
        this.resMessage = resMessage;
        this.assetIssueID = assetIssueID;
        this.withdrawAmount = withdrawAmount;
        this.unfreezeAmount = unfreezeAmount;
        this.internalTransactions = internalTransactions;
        this.exchangeReceivedAmount = exchangeReceivedAmount;
        this.exchangeInjectAnotherAmount = exchangeInjectAnotherAmount;
        this.exchangeWithdrawAnotherAmount = exchangeWithdrawAnotherAmount;
        this.exchangeId = exchangeId;
        this.contracts = contracts;
    }
}
