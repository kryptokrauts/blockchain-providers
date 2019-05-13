package network.arkane.provider.neo.wallet.decryption;

import io.neow3j.crypto.ECKeyPair;

import io.neow3j.crypto.WalletFile;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.wallet.decryption.WalletDecryptor;
import org.springframework.stereotype.Component;

import io.neow3j.crypto.Wallet;

@Component
public class NeoWalletDecryptor implements WalletDecryptor<GeneratedNeoWallet, NeoSecretKey> {
    @Override
    public NeoSecretKey generateKey(GeneratedNeoWallet generatedWallet, String password) {
        try {
            final WalletFile walletFile = generatedWallet.getWalletFile();
            final WalletFile.Account account = walletFile.getAccounts().get(0);

            final ECKeyPair keyPair = Wallet.decryptStandard(password, walletFile, account);
            return NeoSecretKey.builder().key(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
