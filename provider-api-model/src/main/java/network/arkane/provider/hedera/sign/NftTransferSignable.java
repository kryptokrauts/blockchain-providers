package network.arkane.provider.hedera.sign;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class NftTransferSignable extends HederaTransferSignable {
    private final String tokenId;
    private final Long serialNumber;

    @Builder
    public NftTransferSignable(final String from, final String to, final String spender, final String tokenId, final Long serialNumber, final String transactionMemo) {
        super(spender, from, to, transactionMemo);
        this.tokenId = tokenId;
        this.serialNumber = serialNumber;
    }
}


