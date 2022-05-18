package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.NftId;
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
public class NftTransferSigner extends HederaSigner<NftTransferSignable, TransferTransaction> implements Signer<NftTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public NftTransferSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(NftTransferSignable signable,
                                                                 HederaSecretKey key) {
        final AccountId toAccount = AccountId.fromString(signable.getTo());
        super.checkTokenAssociation(toAccount, TokenId.fromString(signable.getTokenId()), clientFactory.getClientWithOperator());
        final TransferTransaction transferTransaction = new TransferTransaction()
                .addNftTransfer(NftId.fromString(signable.getTokenId() + "/" + signable.getSerialNumber()),
                                AccountId.fromString(signable.getFrom()),
                                toAccount);
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                                  .sign(key.getKey());
    }

}
