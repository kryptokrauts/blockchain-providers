package network.arkane.provider.tx.imx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImxTransactionInfoData {

    private ImxTransactionInfoProperties properties;
    private int decimals;
    private String id;
    private String quantity;
    private String quantity_with_fees;
    private String token_address;
    private String token_id;
}
