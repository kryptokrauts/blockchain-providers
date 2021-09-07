package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HbarTransferSigner extends HederaSigner<HbarTransferSignable, TransferTransaction> implements Signer<HbarTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public HbarTransferSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(HbarTransferSignable signable,
                                                                 HederaSecretKey key) {
        Hbar amount = Hbar.fromTinybars(signable.getAmount().longValueExact());
        return new TransferTransaction()
                .addHbarTransfer(AccountId.fromString(signable.getFrom()), amount.negated())
                .addHbarTransfer(AccountId.fromString(signable.getTo()), amount)
                .freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                .sign(key.getKey());
    }

}
