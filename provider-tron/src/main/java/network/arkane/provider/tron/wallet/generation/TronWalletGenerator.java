package network.arkane.provider.tron.wallet.generation;

import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;
import org.tron.keystore.CipherException;
import org.tron.keystore.Wallet;
import org.tron.keystore.WalletFile;

@Component
public class TronWalletGenerator implements WalletGenerator<TronSecretKey> {

    @Override
    public GeneratedWallet generateWallet(String password, TronSecretKey secret) {
        try {
            final WalletFile wallet = Wallet.createStandard(password, secret.getKeyPair());
            return GeneratedTronWallet.builder()
                                      .address(wallet.getAddress())
                                      .walletFile(wallet)
                                      .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }
}
