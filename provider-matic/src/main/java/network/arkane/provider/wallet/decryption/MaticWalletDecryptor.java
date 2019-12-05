package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.MaticSecretKey;
import network.arkane.provider.wallet.generation.GeneratedMaticWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;

@Component
public class MaticWalletDecryptor implements WalletDecryptor<GeneratedMaticWallet, MaticSecretKey> {
    @Override
    public MaticSecretKey generateKey(GeneratedMaticWallet generatedWallet,
                                      String password) {
        try {
            final ECKeyPair keyPair = Wallet.decrypt(password, generatedWallet.getWalletFile());
            return MaticSecretKey.builder().keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
