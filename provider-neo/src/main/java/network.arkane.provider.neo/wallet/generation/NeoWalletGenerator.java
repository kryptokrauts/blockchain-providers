package network.arkane.provider.neo.wallet.generation;

import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.neow3j.crypto.Keys;
import io.neow3j.crypto.Wallet;
import io.neow3j.crypto.WalletFile;
import io.neow3j.crypto.exceptions.CipherException;


@Component
public class NeoWalletGenerator implements WalletGenerator<NeoSecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password, final NeoSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        try {
            WalletFile wallet = Wallet.createStandardWallet();
            WalletFile.Account account = Wallet.createStandardAccount(password, secret.getKey());
            wallet.addAccount(account);

            return GeneratedNeoWallet
                    .builder()
                    .walletFile(wallet)
                    .address(getAddress(secret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final NeoSecretKey neoSecret) {
        return  Keys.getAddress(neoSecret.getKey());
    }
}
