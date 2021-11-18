package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NftCreationSigner extends HederaSigner<NftCreationSignable, TokenCreateTransaction> implements Signer<NftCreationSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public NftCreationSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TokenCreateTransaction> createTransaction(NftCreationSignable signable,
                                                                    HederaSecretKey key) {
        AccountId from = AccountId.fromString(signable.getFrom());
        return new TokenCreateTransaction()
                .setTokenType(TokenType.NON_FUNGIBLE_UNIQUE)
                .setInitialSupply(0)
                .setTreasuryAccountId(StringUtils.isBlank(signable.getTreasuryAccountId()) ? from : AccountId.fromString(signable.getTreasuryAccountId()))
                .setDecimals(0)
                .setTokenName(signable.getName())
                .setTokenMemo(signable.getMemo())
                .setTokenSymbol(signable.getSymbol())
                .freezeWith(clientFactory.buildClient(from, key.getKey()))
                .sign(key.getKey());
    }
}
