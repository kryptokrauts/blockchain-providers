package network.arkane.provider.hedera.wallet;

import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaKeystore;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.hedera.mirror.dto.Account;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
public class HederaWalletGenerator implements WalletGenerator<HederaSecretKey> {

    private final Client client;
    private final MirrorNodeClient mirrorNodeClient;

    public HederaWalletGenerator(HederaClientFactory clientFactory,
                                 MirrorNodeClient mirrorNodeClient) {
        this.client = clientFactory.getClientWithOperator();
        this.mirrorNodeClient = mirrorNodeClient;
    }

    @Override
    public GeneratedWallet generateWallet(String password,
                                          HederaSecretKey secret) {
        HederaKeystore keystore = new HederaKeystore(secret.getKey());
        try {
            String accountId = getExistingAccount(secret).orElseGet(() -> createAccount(secret));
            return GeneratedHederaWallet.builder()
                                        .address(accountId)
                                        .keystore(keystore.export(password))
                                        .build();
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("A problem occurred trying to create hedera wallet")
                    .cause(e)
                    .build();
        }
    }

    private String createAccount(HederaSecretKey secret) {
        try {
            TransactionResponse transactionResponse = new AccountCreateTransaction()
                    .setKey(secret.getKey())
                    .execute(client);
            TransactionReceipt receipt = transactionResponse.getReceipt(client);
            return receipt.accountId.toString();
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("A problem occurred trying to create an account for your hedera wallet")
                    .cause(e)
                    .build();
        }

    }

    private Optional<String> getExistingAccount(HederaSecretKey secretKey) {
        try {
            return mirrorNodeClient.getAccounts(secretKey.getKey().getPublicKey().toString())
                                   .getAccounts().stream()
                                   .filter(a -> BooleanUtils.isFalse(a.getDeleted()))
                                   .map(Account::getAccount)
                                   .findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
