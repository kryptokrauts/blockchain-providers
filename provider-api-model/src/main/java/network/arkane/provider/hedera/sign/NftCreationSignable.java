package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

@Data
@Builder
public class NftCreationSignable implements Signable {
    private String from;
    private String treasuryAccountId;
    private String name;
    private String symbol;
    private String memo;
}


