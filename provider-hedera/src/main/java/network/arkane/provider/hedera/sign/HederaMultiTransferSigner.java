package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.TransferHandler;
import network.arkane.provider.sign.Signer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HederaMultiTransferSigner extends HederaSigner<HederaMultiTransferSignable, TransferTransaction> implements Signer<HederaMultiTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;
    private Map<Class<? extends HederaTransferSignable>, TransferHandler<? extends HederaTransferSignable>> transferHandlerMap;

    public HederaMultiTransferSigner(final HederaClientFactory clientFactory,
                                     @Qualifier("transferHandlerMap")
                                     final Map<Class<? extends HederaTransferSignable>, TransferHandler<? extends HederaTransferSignable>> transferHandlerMap) {
        this.clientFactory = clientFactory;
        this.transferHandlerMap = transferHandlerMap;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(final HederaMultiTransferSignable signable,
                                                                 final HederaSecretKey key) {
        final TransferTransaction transferTransaction = new TransferTransaction();
        signable.getTransfers()
                .forEach(hederaTransferSignable -> transferHandlerMap.get(hederaTransferSignable.getClass())
                                                                     .addTransfer(transferTransaction, hederaTransferSignable));
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey())).sign(key.getKey());
    }
}
