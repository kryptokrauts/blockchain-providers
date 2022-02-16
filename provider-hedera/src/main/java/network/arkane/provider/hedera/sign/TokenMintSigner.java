package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class TokenMintSigner extends HederaSigner<HederaTokenMintSignable, TokenMintTransaction> implements Signer<HederaTokenMintSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public TokenMintSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TokenMintTransaction> createTransaction(HederaTokenMintSignable signable,
                                                                  HederaSecretKey key) {
        return new TokenMintTransaction()
                .setTokenId(TokenId.fromString(signable.getTokenId()))
                .setMetadata(Collections.singletonList(signable.getMetadata().getBytes()))
                .setAmount(signable.getAmount())
                .freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getAccountId()), key.getKey()))
                .sign(key.getKey());
    }
}
