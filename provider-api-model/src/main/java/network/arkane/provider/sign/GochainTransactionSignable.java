package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class GochainTransactionSignable implements Signable {

    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private BigInteger nonce;
    private BigInteger value;
    private String data;
    protected String to;

    @Builder
    public GochainTransactionSignable(BigInteger gasPrice, BigInteger gasLimit, BigInteger nonce, BigInteger value, String data, String to) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.to = to;
    }
}
