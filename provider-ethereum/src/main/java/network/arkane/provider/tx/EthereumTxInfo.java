package network.arkane.provider.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@ToString
public class EthereumTxInfo extends TxInfo {

    private BigInteger nonce;
    private BigInteger gas;
    private BigInteger gasUsed;
    private BigInteger gasPrice;
    private List<EthereumTxLog> logs;

    protected EthereumTxInfo() {
    }

    @Builder(builderMethodName = "ethereumTxInfoBuilder")
    public EthereumTxInfo(String hash,
                          TxStatus status,
                          String from,
                          String to,
                          BigInteger confirmations,
                          String blockHash,
                          BigInteger blockNumber,
                          BigInteger nonce,
                          BigInteger gas, BigInteger gasUsed, BigInteger gasPrice, List<EthereumTxLog> logs) {
        super(hash, status, from, to, confirmations, blockHash, blockNumber);
        this.nonce = nonce;
        this.gas = gas;
        this.gasUsed = gasUsed;
        this.gasPrice = gasPrice;
        this.logs = logs;
    }
}
