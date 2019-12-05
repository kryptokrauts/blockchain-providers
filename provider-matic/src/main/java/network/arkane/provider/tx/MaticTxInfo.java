package network.arkane.provider.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@ToString
public class MaticTxInfo extends TxInfo {

    private BigInteger nonce;
    private BigInteger gas;
    private BigInteger gasUsed;
    private BigInteger gasPrice;
    private List<MaticTxLog> logs;
    private String from;
    private String to;

    protected MaticTxInfo() {
    }

    @Builder(builderMethodName = "ethereumTxInfoBuilder")
    public MaticTxInfo(String hash,
                       TxStatus status,
                       String from,
                       String to,
                       BigInteger confirmations,
                       String blockHash,
                       BigInteger blockNumber,
                       BigInteger nonce,
                       BigInteger gas, BigInteger gasUsed, BigInteger gasPrice, List<MaticTxLog> logs) {
        super(hash, status, confirmations, blockHash, blockNumber);
        this.nonce = nonce;
        this.gas = gas;
        this.gasUsed = gasUsed;
        this.gasPrice = gasPrice;
        this.logs = logs;
        this.from = from;
        this.to = to;
    }
}
