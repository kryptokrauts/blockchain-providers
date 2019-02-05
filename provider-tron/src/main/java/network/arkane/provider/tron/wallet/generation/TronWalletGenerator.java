package network.arkane.provider.tron.wallet.generation;

import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;

@Component
public class TronWalletGenerator implements WalletGenerator<TronSecretKey> {

    @Override
    public GeneratedWallet generateWallet(String password, TronSecretKey secret) {
        return null;
    }
}
