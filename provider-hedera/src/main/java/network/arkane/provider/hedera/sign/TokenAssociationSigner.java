package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenAssociationSigner extends HederaSigner<TokenAssociationSignable, TokenAssociateTransaction> implements Signer<TokenAssociationSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public TokenAssociationSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TokenAssociateTransaction> createTransaction(TokenAssociationSignable signable,
                                                                       HederaSecretKey key) {
        return new TokenAssociateTransaction()
                .setTokenIds(CollectionUtils.emptyIfNull(signable.getTokenIds()).stream().map(TokenId::fromString).collect(Collectors.toList()))
                .freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getAccountId()), key.getKey()))
                .sign(key.getKey());
    }

}
