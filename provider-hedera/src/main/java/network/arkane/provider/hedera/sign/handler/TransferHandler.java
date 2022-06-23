package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.HederaTransferSignable;
import org.apache.commons.lang3.StringUtils;

public abstract class TransferHandler<T extends HederaTransferSignable> {

    public void addTransfer(final TransferTransaction transferTransaction,
                            final HederaTransferSignable transferSignable) {
        if (StringUtils.isBlank(transferSignable.getSpender())) {
            addRegularTransfer(transferTransaction, (T) transferSignable);
        } else {
            addApprovedTransfer(transferTransaction, (T) transferSignable);
        }
    }

    abstract void addRegularTransfer(final TransferTransaction transferTransaction,
                                     final T tokenTransferSignable);

    abstract void addApprovedTransfer(final TransferTransaction transferTransaction,
                                      final T tokenTransferSignable);

    protected AccountId extractFrom(T signable) {
        return AccountId.fromString(signable.getFrom());
    }

    protected AccountId extractTo(T signable) {
        return AccountId.fromString(signable.getTo());
    }
}
