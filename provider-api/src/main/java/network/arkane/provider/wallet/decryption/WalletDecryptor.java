package network.arkane.provider.wallet.decryption;

import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;

public interface WalletDecryptor<T extends GeneratedWallet, KEY extends SecretKey> {
    KEY generateKey(final T generatedWallet, final String password);
}
