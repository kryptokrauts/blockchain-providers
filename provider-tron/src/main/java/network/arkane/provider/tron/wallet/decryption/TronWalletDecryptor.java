package network.arkane.provider.tron.wallet.decryption;

import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.wallet.generation.GeneratedTronWallet;
import network.arkane.provider.wallet.decryption.WalletDecryptor;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.keystore.Wallet;

@Component
public class TronWalletDecryptor implements WalletDecryptor<GeneratedTronWallet, TronSecretKey> {
    @Override
    public TronSecretKey generateKey(GeneratedTronWallet generatedWallet, String password) {
        try {
            final ECKey keyPair = (ECKey) Wallet.decrypt(password, generatedWallet.getWalletFile());
            return TronSecretKey.builder().keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
