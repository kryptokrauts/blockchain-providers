package network.arkane.provider.aeternity.sign;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class AeternitySpendTransactionSignable implements Signable {

    private String sender;
    private String recipient;
    private BigInteger amount;
    @Default
    private String payload = "";
    @Default
    private BigInteger ttl = BigInteger.ZERO;
    private BigInteger nonce;

    @Builder
    public AeternitySpendTransactionSignable(String sender, String recipient, BigInteger amount, String payload, BigInteger ttl, BigInteger nonce) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.payload = payload;
        this.ttl = ttl;
        this.nonce = nonce;
    }
}
