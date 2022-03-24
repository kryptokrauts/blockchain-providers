package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransferTransaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
public class HederaMultiTransferSigner extends HederaSigner<HederaMultiTransferSignable, TransferTransaction> implements Signer<HederaMultiTransferSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public HederaMultiTransferSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<TransferTransaction> createTransaction(final HederaMultiTransferSignable signable,
                                                                 final HederaSecretKey key) {
        final TransferTransaction transferTransaction = new TransferTransaction();
        signable.getTransfers()
                .forEach(hederaTransferSignable -> this.mapTransfer(transferTransaction, hederaTransferSignable));
        if (signable.getTransactionMemo() != null) {
            transferTransaction.setTransactionMemo(signable.getTransactionMemo());
        }
        return transferTransaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getFrom()), key.getKey()))
                                  .sign(key.getKey());
    }

    private void mapTransfer(final TransferTransaction transferTransaction,
                             final HederaTransferSignable signable) {
        mapIfInstanceOf(HbarTransferSignable.class, signable, hbarTransferSignable -> {
            Hbar amount = Hbar.fromTinybars(hbarTransferSignable.getAmount().longValueExact());
            transferTransaction.addHbarTransfer(AccountId.fromString(hbarTransferSignable.getFrom()), amount.negated());
            transferTransaction.addHbarTransfer(AccountId.fromString(hbarTransferSignable.getTo()), amount);
        });

        mapIfInstanceOf(TokenTransferSignable.class, signable, tokenTransferSignable -> {
            transferTransaction.addTokenTransfer(TokenId.fromString(tokenTransferSignable.getTokenId()),
                                                 AccountId.fromString(tokenTransferSignable.getFrom()),
                                                 tokenTransferSignable.getAmount().negate().longValueExact());
            transferTransaction.addTokenTransfer(TokenId.fromString(tokenTransferSignable.getTokenId()),
                                                 AccountId.fromString(tokenTransferSignable.getTo()),
                                                 tokenTransferSignable.getAmount().longValueExact());
        });

        mapIfInstanceOf(NftTransferSignable.class, signable, nftTransferSignable -> {
            final NftId nftId = NftId.fromString(nftTransferSignable.getTokenId() + "/" + nftTransferSignable.getSerialNumber());
            transferTransaction.addNftTransfer(nftId, AccountId.fromString(nftTransferSignable.getFrom()),
                                               AccountId.fromString(nftTransferSignable.getTo()));
        });
    }

    private <T> void mapIfInstanceOf(final Class<T> tClass,
                                     final Signable signable,
                                     final Consumer<T> mapper) {
        Optional.of(signable)
                .filter(tClass::isInstance)
                .map(tClass::cast)
                .ifPresent(mapper);
    }
}
