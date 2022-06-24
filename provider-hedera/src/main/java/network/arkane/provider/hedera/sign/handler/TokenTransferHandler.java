package network.arkane.provider.hedera.sign.handler;

import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.sign.HederaTransferSignable;
import network.arkane.provider.hedera.sign.TokenTransferSignable;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TokenTransferHandler extends TransferHandlerTemplate<TokenTransferSignable> {

    @Override
    public Class<? extends HederaTransferSignable> forClass() {
        return TokenTransferSignable.class;
    }

    @Override
    protected void addRegularTransfer(final TransferTransaction transferTransaction,
                                      final TokenTransferSignable signable) {
        final BigInteger amount = signable.getAmount();
        transferTransaction.addTokenTransfer(TokenId.fromString(signable.getTokenId()), extractFrom(signable), amount.negate().longValueExact());
        transferTransaction.addTokenTransfer(TokenId.fromString(signable.getTokenId()), extractTo(signable), amount.longValueExact());
    }

    @Override
    protected void addApprovedTransfer(final TransferTransaction transferTransaction,
                                    final TokenTransferSignable signable) {
        final BigInteger amount = signable.getAmount();
        transferTransaction.addApprovedTokenTransfer(TokenId.fromString(signable.getTokenId()), extractFrom(signable), amount.negate().longValueExact());
        transferTransaction.addApprovedTokenTransfer(TokenId.fromString(signable.getTokenId()), extractTo(signable), amount.longValueExact());
    }
}
