package network.arkane.provider.tx.imx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImxTransactionTokenInfo {

    private ImxTransactionInfoData data;
    private String type;
}
