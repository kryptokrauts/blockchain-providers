package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.Transaction;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signable;
import network.arkane.provider.sign.domain.TransactionSignature;

import java.util.Base64;

public abstract class HederaSigner<S extends Signable, T extends Transaction<T>> implements Signer<S, HederaSecretKey> {

    protected abstract Transaction<T> createTransaction(S signable,
                                                        HederaSecretKey key);

    @Override
    public TransactionSignature createSignature(S signable,
                                                HederaSecretKey key) {
        Transaction<T> transaction = createTransaction(signable, key);
        byte[] bytes = transaction.toBytes();
        String value = Base64.getEncoder().encodeToString(bytes);

        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(value)
                .build();
    }
}
