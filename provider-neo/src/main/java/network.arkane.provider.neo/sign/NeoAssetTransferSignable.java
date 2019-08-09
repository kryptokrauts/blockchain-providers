package network.arkane.provider.neo.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
public class NeoAssetTransferSignable implements Signable {

    private String to;
    private BigDecimal amount;

    @Builder
    public NeoAssetTransferSignable(String to, BigDecimal amount) {
        this.to = to;
        this.amount = amount;
    }
}
