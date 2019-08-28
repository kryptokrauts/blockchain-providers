package network.arkane.provider.neo.wallet.decryption;

import io.neow3j.crypto.ECKeyPair;

import io.neow3j.crypto.NEP2;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.wallet.decryption.WalletDecryptor;
import org.springframework.stereotype.Component;


@Component
public class NeoWalletDecryptor implements WalletDecryptor<GeneratedNeoWallet, NeoSecretKey> {
    @Override
    public NeoSecretKey generateKey(GeneratedNeoWallet generatedWallet, String password) {
        try {
            final Wallet wallet = Wallet.fromNEP6Wallet(generatedWallet.getWalletFile()).build();
            final Account account = wallet.getAccounts().get(0);
            account.decryptPrivateKey(password, NEP2.DEFAULT_SCRYPT_PARAMS);
            final ECKeyPair keyPair = account.getECKeyPair();
            return NeoSecretKey.builder().key(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
