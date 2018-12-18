package network.arkane.provider.wallet.decryption;

import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;

public interface WalletDecryptor<T extends GeneratedWallet, KEY extends SecretKey> {

    /**
     * Decrypt a wallet, based on a generated wallet and password
     * @param generatedWallet
     * @param password
     * @return
     */
    KEY generateKey(final T generatedWallet, final String password);
}
