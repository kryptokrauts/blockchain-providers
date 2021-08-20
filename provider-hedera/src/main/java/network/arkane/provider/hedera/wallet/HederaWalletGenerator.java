package network.arkane.provider.hedera.wallet;

import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaKeystore;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
public class HederaWalletGenerator implements WalletGenerator<HederaSecretKey> {

    private final Client client;

    public HederaWalletGenerator(HederaClientFactory clientFactory) {
        this.client = clientFactory.getClientWithOperator();
    }

    @Override
    public GeneratedWallet generateWallet(String password,
                                          HederaSecretKey secret) {
        HederaKeystore keystore = new HederaKeystore(secret.getKey());
        try {
            TransactionResponse transactionResponse = new AccountCreateTransaction()
                    .setKey(secret.getKey())
                    .setAccountMemo("Account of https://venly.io")
                    .setTransactionMemo("Account creation by https://venly.io")
                    .execute(client);
            TransactionReceipt receipt = transactionResponse.getReceipt(client);
            return GeneratedHederaWallet.builder()
                                        .address(receipt.accountId.toString())
                                        .keystore(keystore.export(password))
                                        .build();
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("A problem occurred trying to create hedera wallet")
                    .cause(e)
                    .build();
        }
    }
}
