package network.arkane.provider.aeternity.wallet.exporting;

import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreService;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.decryption.AeternityWalletDecryptor;
import network.arkane.provider.aeternity.wallet.generation.GeneratedAeternityWallet;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;

@Component
public class AeternityKeystoreExporter implements KeyExporter<AeternitySecretKey> {

    private final KeystoreService keystoreService = new KeystoreServiceFactory().getService();
    private final AeternityWalletDecryptor aeternityWalletDecryptor;

    public AeternityKeystoreExporter(AeternityWalletDecryptor aeternityWalletDecryptor) {
        this.aeternityWalletDecryptor = aeternityWalletDecryptor;
    }

    @Override
    public String export(AeternitySecretKey key, final String password) {
        try {
            return keystoreService.createKeystore(key.getKeyPair(), password, null);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                    .errorCode("export.aeternity")
                    .message("An error occurred while trying to export the key")
                    .build();
        }
    }

    @Override
    public AeternitySecretKey reconstructKey(String secret, String password) {
        return aeternityWalletDecryptor.generateKey(GeneratedAeternityWallet.builder()
                .keystoreJson(secret)
                .build(), password);
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
