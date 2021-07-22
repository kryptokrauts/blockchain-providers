package network.arkane.provider.hedera.wallet;

import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaKeystore;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

public class HederaWalletGenerator implements WalletGenerator<HederaSecretKey> {

    private final Client client;

    public HederaWalletGenerator(Client hederaClientWithOperator) {
        this.client = hederaClientWithOperator;
    }

    @Override
    public GeneratedWallet generateWallet(String password,
                                          HederaSecretKey secret) {
        HederaKeystore keystore = new HederaKeystore(secret.getKey());
        try {
            TransactionResponse transactionResponse = new AccountCreateTransaction()
                    .setKey(secret.getKey())
                    .setAccountMemo("Account of https://arkane.network")
                    .setTransactionMemo("Account creation by https://arkane.network")
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
