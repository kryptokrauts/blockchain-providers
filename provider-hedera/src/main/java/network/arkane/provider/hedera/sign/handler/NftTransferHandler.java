package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.HederaTransferSignable;
import network.arkane.provider.hedera.sign.NftTransferSignable;
import org.springframework.stereotype.Component;

@Component
public class NftTransferHandler extends TransferHandlerTemplate<NftTransferSignable> {

    @Override
    public Class<? extends HederaTransferSignable> forClass() {
        return NftTransferSignable.class;
    }

    @Override
    protected void addRegularTransfer(final TransferTransaction transferTransaction,
                                      final NftTransferSignable nftTransferSignable) {
        transferTransaction.addNftTransfer(buildNftId(nftTransferSignable), extractFrom(nftTransferSignable), extractTo(nftTransferSignable));
    }

    @Override
    protected void addApprovedTransfer(final TransferTransaction transferTransaction,
                                    final NftTransferSignable nftTransferSignable) {
        transferTransaction.addApprovedNftTransfer(buildNftId(nftTransferSignable), extractFrom(nftTransferSignable), extractTo(nftTransferSignable));
    }

    private NftId buildNftId(final NftTransferSignable nftTransferSignable) {
        return NftId.fromString(nftTransferSignable.getTokenId() + "/" + nftTransferSignable.getSerialNumber());
    }
}
