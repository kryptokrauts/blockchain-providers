package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class AeternitySpendTransactionSignable implements Signable {

    private String sender;
    private String recipient;
    private BigInteger amount;
    private String payload;
    private BigInteger fee;
    private BigInteger ttl;
    private BigInteger nonce;

    @Builder
    public AeternitySpendTransactionSignable( String sender, String recipient, BigInteger amount, String payload, BigInteger fee, BigInteger ttl, BigInteger nonce ) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.payload = payload;
        this.fee = fee;
        this.ttl = ttl;
        this.nonce = nonce;
    }
}
