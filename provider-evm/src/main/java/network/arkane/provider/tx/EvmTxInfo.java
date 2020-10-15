package network.arkane.provider.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@ToString
public class EvmTxInfo extends TxInfo {

    private BigInteger nonce;
    private BigInteger gas;
    private BigInteger gasUsed;
    private BigInteger gasPrice;
    private List<EvmTxLog> logs;
    private String from;
    private String to;

    protected EvmTxInfo() {
    }

    @Builder(builderMethodName = "evmTxInfoBuilder")
    public EvmTxInfo(String hash,
                     TxStatus status,
                     String from,
                     String to,
                     BigInteger confirmations,
                     String blockHash,
                     BigInteger blockNumber,
                     BigInteger nonce,
                     BigInteger gas,
                     BigInteger gasUsed,
                     BigInteger gasPrice,
                     List<EvmTxLog> logs) {
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
