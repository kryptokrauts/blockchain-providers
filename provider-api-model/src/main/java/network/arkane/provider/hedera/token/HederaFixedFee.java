package network.arkane.provider.hedera.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Optional;

import static network.arkane.provider.hedera.token.HederaFeeType.FIXED_FEE;

@Data
@EqualsAndHashCode(callSuper = true)
public class HederaFixedFee extends HederaCustomFee {

    private String feeCollectorAccountId;

    private BigDecimal hbarAmount;

    private long amount;

    private String denominatingTokenId;

    @Builder
    public HederaFixedFee(final String feeCollectorAccountId,
                          final BigDecimal hbarAmount,
                          final long amount,
                          final String denominatingTokenId) {
        super(FIXED_FEE);
        this.feeCollectorAccountId = feeCollectorAccountId;
        this.hbarAmount = hbarAmount;
        this.amount = amount;
        this.denominatingTokenId = denominatingTokenId;
    }

    @Override
    public Optional<HederaFixedFee> toFixedFee() {
        return Optional.of(this);
    }
}
