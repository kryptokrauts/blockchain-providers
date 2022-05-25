package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.hedera.token.HederaAllowanceType;
import network.arkane.provider.sign.domain.Signable;

@Data
@Builder
public class ApproveAllowanceSignable implements Signable {

    private String accountId;
    private HederaAllowanceType allowanceType;
    private String spenderAccountId;
    private Long amount;
    private String tokenId;
    private Long serial;
}
