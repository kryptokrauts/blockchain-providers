package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.HbarTransferSignable;
import org.springframework.stereotype.Component;

@Component
public class HbarTransferHandler extends TransferHandler<HbarTransferSignable> {

    public void addRegularTransfer(final TransferTransaction transferTransaction,
                                   final HbarTransferSignable signable) {
        final Hbar amount = Hbar.fromTinybars(signable.getAmount().longValueExact());
        transferTransaction.addHbarTransfer(extractFrom(signable), amount.negated());
        transferTransaction.addHbarTransfer(extractTo(signable), amount);
    }

    public void addApprovedTransfer(final TransferTransaction transferTransaction,
                                    final HbarTransferSignable signable) {
        final Hbar amount = Hbar.fromTinybars(signable.getAmount().longValueExact());
        transferTransaction.addApprovedHbarTransfer(extractFrom(signable), amount.negated());
        transferTransaction.addApprovedHbarTransfer(extractTo(signable), amount);
    }

}
