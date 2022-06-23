package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.NftTransferSignable;
import org.springframework.stereotype.Component;

@Component
public class NftTransferHandler extends TransferHandler<NftTransferSignable> {

    public void addRegularTransfer(final TransferTransaction transferTransaction,
                                   final NftTransferSignable nftTransferSignable) {
        transferTransaction.addNftTransfer(buildNftId(nftTransferSignable), extractFrom(nftTransferSignable), extractTo(nftTransferSignable));
    }

    public void addApprovedTransfer(final TransferTransaction transferTransaction,
                                    final NftTransferSignable nftTransferSignable) {
        transferTransaction.addApprovedNftTransfer(buildNftId(nftTransferSignable), extractFrom(nftTransferSignable), extractTo(nftTransferSignable));
    }

    private NftId buildNftId(final NftTransferSignable nftTransferSignable) {
        return NftId.fromString(nftTransferSignable.getTokenId() + "/" + nftTransferSignable.getSerialNumber());
    }
}
