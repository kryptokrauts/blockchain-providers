package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.NftTransferHandler;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NftSingleTransferSigner extends HederaSingleTransferSigner<NftTransferSignable> implements Signer<NftTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public NftSingleTransferSigner(final HederaClientFactory clientFactory,
                                   final NftTransferHandler nftTransferHandler) {
        super(clientFactory, nftTransferHandler);
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(final NftTransferSignable signable,
                                                                 final HederaSecretKey key) {
        final AccountId toAccount = AccountId.fromString(signable.getTo());
        super.checkTokenAssociation(toAccount, TokenId.fromString(signable.getTokenId()), clientFactory.getClientWithOperator());
        return super.createTransaction(signable, key);
    }
}
