package network.arkane.provider.tx.imx;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImxTransactionTransferInfo extends ImxTransactionInfo {
    private String receiver;
    private ImxTransactionInfoData data;
}
