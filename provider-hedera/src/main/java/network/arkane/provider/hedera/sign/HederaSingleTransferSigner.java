package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.TransferHandlerTemplate;
import org.apache.commons.lang3.StringUtils;

public abstract class HederaSingleTransferSigner<S extends HederaTransferSignable> extends HederaSigner<S, TransferTransaction> {
    private HederaClientFactory clientFactory;
    private TransferHandlerTemplate<S> transferHandler;

    public HederaSingleTransferSigner(final HederaClientFactory clientFactory,
                                      final TransferHandlerTemplate<S> transferHandler) {
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
        final String operator = StringUtils.isNotBlank(signable.getSpender()) ? signable.getSpender() : signable.getFrom();
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(operator), key.getKey()))
                                  .sign(key.getKey());
    }
}
