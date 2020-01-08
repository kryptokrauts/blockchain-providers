package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@ToString
public class TronTransactionSignable implements Signable {

    private BigInteger amount;
    private String to;
    private String data;

    @Builder
    public TronTransactionSignable(final String to,
                                   final BigInteger amount,
                                   final String data) {
        this.to = to;
        this.amount = amount;
        this.data = data;
    }
}
