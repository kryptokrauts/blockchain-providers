package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.wallet.generation.GeneratedVechainWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;

@Component
public class VechainWalletDecryptor implements WalletDecryptor<GeneratedVechainWallet, VechainSecretKey> {

    @Override
    public VechainSecretKey generateKey(GeneratedVechainWallet generatedWallet, String password) {
        try {
            final ECKeyPair keyPair = Wallet.decrypt(password, generatedWallet.getWalletFile());
            return VechainSecretKey.builder().keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch secret");
        }
    }
}
