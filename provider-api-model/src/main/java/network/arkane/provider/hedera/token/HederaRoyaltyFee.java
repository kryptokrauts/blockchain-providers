package network.arkane.provider.hedera.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

import static network.arkane.provider.hedera.token.HederaFeeType.ROYALTY_FEE;

@Data
@EqualsAndHashCode(callSuper = true)
public class HederaRoyaltyFee extends HederaCustomFee {

    private long numerator;

    private long denominator;

    private HederaFixedFee fallbackFee;

    private String feeCollectorAccountId;

    @Builder
    public HederaRoyaltyFee(final long numerator,
                            final long denominator,
                            final HederaFixedFee fallbackFee,
                            final String feeCollectorAccountId) {
        super(ROYALTY_FEE);
        this.numerator = numerator;
        this.denominator = denominator;
        this.fallbackFee = fallbackFee;
        this.feeCollectorAccountId = feeCollectorAccountId;
    }

    @Override
    public Optional<HederaRoyaltyFee> toRoyaltyFee() {
        return Optional.of(this);
    }
}
