package network.arkane.provider.hedera.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class HederaCustomFee {

    private HederaFeeType type;

    public Optional<HederaFixedFee> toFixedFee() {
        return Optional.empty();
    }

    public Optional<HederaFractionalFee> toFractionalFee() {
        return Optional.empty();
    }

    public Optional<HederaRoyaltyFee> toRoyaltyFee() {
        return Optional.empty();
    }

}
