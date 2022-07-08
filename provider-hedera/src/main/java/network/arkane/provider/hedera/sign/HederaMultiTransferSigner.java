package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.TransferHandlerTemplate;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HederaMultiTransferSigner extends HederaSigner<HederaMultiTransferSignable, TransferTransaction> implements Signer<HederaMultiTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;
    private Map<Class<? extends HederaTransferSignable>, TransferHandlerTemplate<? extends HederaTransferSignable>> transferHandlerMap;

    public HederaMultiTransferSigner(final HederaClientFactory clientFactory,
                                     final List<TransferHandlerTemplate<? extends HederaTransferSignable>> transferHandlers) {
        this.clientFactory = clientFactory;
        this.transferHandlerMap = transferHandlers.stream().collect(Collectors.toMap(TransferHandlerTemplate::forClass, Function.identity()));
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(final HederaMultiTransferSignable signable,
                                                                 final HederaSecretKey key) {
        final TransferTransaction transferTransaction = new TransferTransaction();
        signable.getTransfers()
                .forEach(hederaTransferSignable -> {
                    this.checkTokenAssociation(hederaTransferSignable);
                    transferHandlerMap.get(hederaTransferSignable.getClass())
                                      .addTransfer(transferTransaction, hederaTransferSignable);
                });
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey())).sign(key.getKey());
    }

    private void checkTokenAssociation(final HederaTransferSignable hederaTransferSignable) {
        if (this.shouldCheckAssociation(hederaTransferSignable)) {
            final AccountId accountId = AccountId.fromString(hederaTransferSignable.getTo());
            final TokenId tokenId = this.extractTokenId(hederaTransferSignable);
            super.checkTokenAssociation(accountId, tokenId, clientFactory.getClientWithOperator());
        }
    }

    private boolean shouldCheckAssociation(final HederaTransferSignable hederaTransferSignable) {
        return hederaTransferSignable instanceof NftTransferSignable ||
               hederaTransferSignable instanceof TokenTransferSignable;
    }

    private TokenId extractTokenId(HederaTransferSignable hederaTransferSignable) {
        if (hederaTransferSignable instanceof NftTransferSignable nftTransferSignable) {
            return TokenId.fromString(nftTransferSignable.getTokenId());
        }
        if (hederaTransferSignable instanceof TokenTransferSignable tokenTransferSignable) {
            return TokenId.fromString(tokenTransferSignable.getTokenId());
        }
        throw new IllegalArgumentException("TokenId cannot be extracted from: " + hederaTransferSignable.getClass().getSimpleName());
    }
}
