package network.arkane.provider.litecoin.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@Builder
public class LitecoinTransactionSignable implements Signable {
    private String address;
    private BigInteger photonValue;
    private int feePerByte;
}
