package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.generation.GeneratedAeternityWallet;
import org.springframework.stereotype.Component;

@Component
public class AeternityWalletDecryptor implements WalletDecryptor<GeneratedAeternityWallet, AeternitySecretKey> {
    @Override
    public AeternitySecretKey generateKey(GeneratedAeternityWallet generatedWallet, String password) {
        // TODO decrypt wallet and generate keypair
        return null;
    }
}
