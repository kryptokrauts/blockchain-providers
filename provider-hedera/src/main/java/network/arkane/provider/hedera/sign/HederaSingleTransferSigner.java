package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.TransferHandler;

public abstract class HederaSingleTransferSigner<S extends HederaTransferSignable> extends HederaSigner<S, TransferTransaction> {
    private HederaClientFactory clientFactory;
    private TransferHandler<S> transferHandler;

    public HederaSingleTransferSigner(final HederaClientFactory clientFactory,
                                      final TransferHandler<S> transferHandler) {
        this.clientFactory = clientFactory;
        this.transferHandler = transferHandler;
    }

    protected Transaction<TransferTransaction> createTransaction(final S signable,
                                                                final HederaSecretKey key) {
        final TransferTransaction transferTransaction = new TransferTransaction();
        transferHandler.addTransfer(transferTransaction, signable);
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                                  .sign(key.getKey());
    }
}
