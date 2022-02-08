package network.arkane.provider.hedera.token;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

import static network.arkane.provider.hedera.token.HederaFeeType.FRACTIONAL_FEE;

@Data
@EqualsAndHashCode(callSuper = true)
public class HederaFractionalFee extends HederaCustomFee {

    private String feeCollectorAccountId;

    private long numerator;

    private long denominator;

    private long max;

    private long min;

    private HederaFeeAssessmentMethod assessmentMethod;

    @Builder
    public HederaFractionalFee(final String feeCollectorAccountId,
                               final long numerator,
                               final long denominator,
                               final long max,
                               final long min,
                               final HederaFeeAssessmentMethod assessmentMethod) {
        super(FRACTIONAL_FEE);
        this.feeCollectorAccountId = feeCollectorAccountId;
        this.numerator = numerator;
        this.denominator = denominator;
        this.max = max;
        this.min = min;
        this.assessmentMethod = assessmentMethod;
    }

    @Override
    public Optional<HederaFractionalFee> toFractionalFee() {
        return Optional.of(this);
    }
}
