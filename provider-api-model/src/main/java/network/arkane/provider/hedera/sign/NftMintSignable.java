package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

@Data
@Builder
public class NftMintSignable implements Signable {
    private String accountId;
    private String tokenId;
    private String metadata;
}


