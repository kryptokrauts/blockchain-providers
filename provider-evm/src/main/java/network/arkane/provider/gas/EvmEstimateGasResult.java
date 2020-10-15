package network.arkane.provider.gas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EvmEstimateGasResult {
    private BigInteger gasLimit;
    private boolean reverted;
}
