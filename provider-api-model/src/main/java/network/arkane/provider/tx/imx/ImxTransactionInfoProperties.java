package network.arkane.provider.tx.imx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImxTransactionInfoProperties {
    private ImxTransactionInfoCollection collection;
    private String image_url;
    private String name;
}
