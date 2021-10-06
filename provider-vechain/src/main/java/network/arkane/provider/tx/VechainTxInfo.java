package network.arkane.provider.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Getter
@ToString
public class VechainTxInfo extends TxInfo {

    private String nonce;
    private BigInteger gas;
    private BigInteger gasUsed;
    private BigInteger gasPriceCoef;
    private List<VechainReceiptOutput> outputs;

    protected VechainTxInfo() {
    }

    @Builder(builderMethodName = "vechainTxInfoBuilder")
    public VechainTxInfo(String hash,
                         TxStatus status,
                         BigInteger confirmations,
                         String blockHash,
                         BigInteger blockNumber,
                         String nonce,
                         BigInteger gas,
                         BigInteger gasUsed,
                         BigInteger gasPriceCoef,
                         List<VechainReceiptOutput> outputs,
                         Boolean hasReachedFinality) {
        super(hash, status, confirmations, blockHash, blockNumber, hasReachedFinality);
        this.nonce = nonce;
        this.gas = gas;
        this.gasUsed = gasUsed;
        this.gasPriceCoef = gasPriceCoef;
        this.outputs = outputs;
    }
}
