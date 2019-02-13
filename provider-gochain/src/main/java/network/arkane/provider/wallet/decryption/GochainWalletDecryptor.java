package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.GochainSecretKey;
import network.arkane.provider.wallet.generation.GeneratedGochainWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;

@Component
public class GochainWalletDecryptor implements WalletDecryptor<GeneratedGochainWallet, GochainSecretKey> {
    @Override
    public GochainSecretKey generateKey(GeneratedGochainWallet generatedWallet, String password) {
        try {
            final ECKeyPair keyPair = Wallet.decrypt(password, generatedWallet.getWalletFile());
            return GochainSecretKey.builder().keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
