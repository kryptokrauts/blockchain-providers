package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenTransferSigner extends HederaSigner<TokenTransferSignable, TransferTransaction> {

    private final HederaClientFactory clientFactory;

    public TokenTransferSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(TokenTransferSignable signable,
                                                                 HederaSecretKey key) {
        return new TransferTransaction()
                .addTokenTransfer(TokenId.fromString(signable.getTokenId()), AccountId.fromString(signable.getFrom()), signable.getAmount().negate().longValue())
                .addTokenTransfer(TokenId.fromString(signable.getTokenId()), AccountId.fromString(signable.getTo()), signable.getAmount().longValue())
                .freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                .sign(key.getKey());
    }

}
