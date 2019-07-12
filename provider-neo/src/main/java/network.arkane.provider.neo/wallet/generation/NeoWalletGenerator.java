package network.arkane.provider.neo.wallet.generation;

import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import network.arkane.provider.neo.NeoW3JConfiguration;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.neow3j.crypto.Keys;
import io.neow3j.crypto.exceptions.CipherException;


@Component
public class NeoWalletGenerator implements WalletGenerator<NeoSecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password, final NeoSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        try {
            Account account = Account.fromECKeyPair(secret.getKey()).build();
            account.encryptPrivateKey(password, NeoW3JConfiguration.defaultScrypt);

            Wallet wallet = new Wallet.Builder()
                    .account(account)
                    .scryptParams(NeoW3JConfiguration.defaultScrypt)
                    .build();

            return GeneratedNeoWallet
                    .builder()
                    .walletFile(wallet.toNEP6Wallet())
                    .address(getAddress(secret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final NeoSecretKey neoSecret) {
        return Keys.getAddress(neoSecret.getKey());
    }
}
