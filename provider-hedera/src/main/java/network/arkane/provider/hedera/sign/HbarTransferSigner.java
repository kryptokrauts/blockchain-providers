package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
public class HbarTransferSigner implements Signer<HbarTransferSignable, HederaSecretKey> {

    private final Client hederaClient;

    public HbarTransferSigner(Client hederaClient) {
        this.hederaClient = hederaClient;
    }

    @Override
    public TransactionSignature createSignature(HbarTransferSignable signable,
                                                HederaSecretKey key) {
        Hbar amount = Hbar.from(signable.getAmount());
        TransferTransaction transferTransaction = new TransferTransaction()
                .addHbarTransfer(AccountId.fromString(signable.getFrom()), amount.negated())
                .addHbarTransfer(AccountId.fromString(signable.getTo()), amount)
                .freezeWith(hederaClient)
                .sign(key.getKey());
        byte[] bytes = transferTransaction.toBytes();
        String value = Base64.getEncoder().encodeToString(bytes);

        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(value)
                .build();
    }
}
