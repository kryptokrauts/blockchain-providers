package network.arkane.provider.tx.imx;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImxTransactionMintInfo extends ImxTransactionInfo {
    private List<ImxTransactionFeeInfo> fees;
    private ImxTransactionTokenInfo token;
}
