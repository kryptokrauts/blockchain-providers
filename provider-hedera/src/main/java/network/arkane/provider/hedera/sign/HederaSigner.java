package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.SneakyThrows;
import network.arkane.provider.exceptions.UserInputException;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.domain.Signable;
import network.arkane.provider.sign.domain.TransactionSignature;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public abstract class HederaSigner<S extends Signable, T extends Transaction<T>> {

    protected abstract Transaction<T> createTransaction(final S signable,
                                                        final HederaSecretKey key);

    public TransactionSignature createSignature(final S signable,
                                                final HederaSecretKey key) {
        final Transaction<T> transaction = createTransaction(signable, key);
        final byte[] bytes = transaction.toBytes();
        final String value = Base64.getEncoder().encodeToString(bytes);

        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(value)
                .build();
    }

    @SneakyThrows
    protected void checkTokenAssociation(final AccountId accountId,
                                         final TokenId tokenId,
                                         final Client client) {
        final AccountInfo accountInfo = new AccountInfoQuery().setAccountId(accountId)
                                                              .execute(client);
        final int tokenRelationshipCount = Optional.ofNullable(accountInfo.tokenRelationships)
                                                   .map(Map::size)
                                                   .orElse(0);
        if (!hasFreeAssociationsAvailable(accountInfo, tokenRelationshipCount) && !hasAssociationForToken(accountInfo, tokenId)) {
            throw new UserInputException("not-associated-error",
                                         String.format("Token '%s' is not associated to account '%s'", tokenId, accountId));
        }
    }

    private boolean hasFreeAssociationsAvailable(final AccountInfo accountInfo,
                                                 final int tokenRelationshipCount) {
        return accountInfo.maxAutomaticTokenAssociations > tokenRelationshipCount;
    }

    private boolean hasAssociationForToken(final AccountInfo accountInfo,
                                           final TokenId tokenId) {
        return Optional.ofNullable(accountInfo.tokenRelationships)
                       .map(Map::entrySet)
                       .filter(entries -> entries.stream()
                                                 .anyMatch(tokenRelationship -> tokenRelationship.getKey().equals(tokenId)))
                       .isPresent();
    }


}
