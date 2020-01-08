package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class EthereumTransactionSignable implements Signable {

    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private BigInteger nonce;
    private BigInteger value;
    private String data;
    protected String to;

    @Builder
    public EthereumTransactionSignable(final BigInteger gasPrice,
                                       final BigInteger gasLimit,
                                       final BigInteger nonce,
                                       final BigInteger value,
                                       final String data,
                                       final String to) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.to = to;
    }
}
