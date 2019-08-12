package network.arkane.provider.neo.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NeoAssetTransferSignable implements Signable {

    private String to;
    private BigDecimal amount;
    private String assetId;

    @Builder
    public NeoAssetTransferSignable(final String to,
                                    final BigDecimal amount,
                                    final String assetId) {
        this.to = to;
        this.amount = amount;
        this.assetId = assetId;
    }
}
