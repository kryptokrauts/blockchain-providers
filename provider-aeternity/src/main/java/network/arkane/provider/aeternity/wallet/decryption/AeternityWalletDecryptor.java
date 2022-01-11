package network.arkane.provider.aeternity.wallet.decryption;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreService;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.generation.GeneratedAeternityWallet;
import network.arkane.provider.wallet.decryption.WalletDecryptor;
import org.springframework.stereotype.Component;

@Component
public class AeternityWalletDecryptor implements WalletDecryptor<GeneratedAeternityWallet, AeternitySecretKey> {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    private final KeystoreService keystoreService = new KeystoreServiceFactory().getService();

    @Override
    public AeternitySecretKey generateKey(GeneratedAeternityWallet generatedWallet, String password) {
        try {
            final String encodedPrivateKey = keystoreService.recoverEncodedPrivateKey(generatedWallet.getKeystoreJson(), password);
            final KeyPair keyPair = keyPairService.recoverKeyPair(encodedPrivateKey);
            return AeternitySecretKey.builder().keyPair(keyPair).build();
        } catch (AException e) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
