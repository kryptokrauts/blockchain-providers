package network.arkane.provider.hedera.sign;

import com.hedera.hashgraph.sdk.AccountAllowanceApproveTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.Transaction;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApproveAllowanceSigner
        extends HederaSigner<ApproveAllowanceSignable, AccountAllowanceApproveTransaction>
        implements Signer<ApproveAllowanceSignable, HederaSecretKey> {

    private final HederaClientFactory clientFactory;

    public ApproveAllowanceSigner(HederaClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Transaction<AccountAllowanceApproveTransaction> createTransaction(ApproveAllowanceSignable signable, HederaSecretKey key) {
        AccountAllowanceApproveTransaction transaction = new AccountAllowanceApproveTransaction();
        transaction = approveTransaction(signable, transaction);
        return transaction.freezeWith(clientFactory.buildClient(AccountId.fromString(signable.getAccountId()), key.getKey())).sign(key.getKey());
    }

    private AccountAllowanceApproveTransaction approveTransaction(ApproveAllowanceSignable signable, AccountAllowanceApproveTransaction transaction) {
        return switch (signable.getAllowanceType()) {
            case HBAR -> transaction.approveHbarAllowance(AccountId.fromString(signable.getAccountId()),
                                                          AccountId.fromString(signable.getSpenderAccountId()),
                                                          Hbar.from(signable.getAmount()));
            case TOKEN -> transaction.approveTokenAllowance(TokenId.fromString(signable.getTokenId()),
                                                            AccountId.fromString(signable.getAccountId()),
                                                            AccountId.fromString(signable.getSpenderAccountId()),
                                                            signable.getAmount());
            case NFT -> transaction.approveTokenNftAllowance(new NftId(TokenId.fromString(signable.getTokenId()), signable.getSerial()),
                                                             AccountId.fromString(signable.getAccountId()),
                                                             AccountId.fromString(signable.getSpenderAccountId()));
            default -> transaction.approveTokenNftAllowanceAllSerials(TokenId.fromString(signable.getTokenId()),
                                                                      AccountId.fromString(signable.getAccountId()),
                                                                      AccountId.fromString(signable.getSpenderAccountId()));
        };
    }
}
