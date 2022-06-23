package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.TokenTransferHandler;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenSingleTransferSigner extends HederaSingleTransferSigner<TokenTransferSignable> implements Signer<TokenTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public TokenSingleTransferSigner(final HederaClientFactory clientFactory,
                                     final TokenTransferHandler tokenTransferHandler) {
        super(clientFactory, tokenTransferHandler);
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(final TokenTransferSignable signable,
                                                                 final HederaSecretKey key) {
        final AccountId toAccount = AccountId.fromString(signable.getTo());
        final TokenId tokenId = TokenId.fromString(signable.getTokenId());
        super.checkTokenAssociation(toAccount, tokenId, clientFactory.getClientWithOperator());
        return super.createTransaction(signable, key);
    }
}
