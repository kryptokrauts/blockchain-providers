package network.arkane.provider.aeternity.sign;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AeternitySpendTransactionSignable implements Signable {

    private String sender;
    private String recipient;
    private BigInteger amount;
    private BigInteger nonce;
    @Default private String payload = "";
    @Default private BigInteger ttl = BigInteger.ZERO;
}
