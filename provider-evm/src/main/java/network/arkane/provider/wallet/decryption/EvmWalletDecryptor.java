package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.wallet.generation.GeneratedEvmWallet;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;

public class EvmWalletDecryptor implements WalletDecryptor<GeneratedEvmWallet, EvmSecretKey> {

    @Override
    public EvmSecretKey generateKey(GeneratedEvmWallet generatedWallet,
                                    String password) {
        try {
            final ECKeyPair keyPair = Wallet.decrypt(password, generatedWallet.getWalletFile());
            return EvmSecretKey.builder().type(generatedWallet.getSecretType()).keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
