package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.Data;
import network.arkane.provider.sign.domain.Signable;

@Data
@Builder
public class NftTransferSignable implements Signable {
    private String from;
    private String to;
    private String nftId;
}


