package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenTransferSigner extends HederaSigner<TokenTransferSignable, TransferTransaction> implements Signer<TokenTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public TokenTransferSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(TokenTransferSignable signable,
                                                                 HederaSecretKey key) {
        final AccountId toAccount = AccountId.fromString(signable.getTo());
        final TokenId tokenId = TokenId.fromString(signable.getTokenId());
        super.checkTokenAssociationAssociation(toAccount, tokenId, clientFactory.getClientWithOperator());
        final TransferTransaction transferTransaction = new TransferTransaction()
                .addTokenTransfer(tokenId, AccountId.fromString(signable.getFrom()), signable.getAmount().negate().longValueExact())
                .addTokenTransfer(tokenId, toAccount, signable.getAmount().longValueExact());
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                                  .sign(key.getKey());
    }

}
