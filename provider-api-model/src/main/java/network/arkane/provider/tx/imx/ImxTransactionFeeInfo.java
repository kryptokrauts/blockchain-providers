package network.arkane.provider.tx.imx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImxTransactionFeeInfo {
    private String address;
    private int percentage;
    private String type;
}
