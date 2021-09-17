package network.arkane.provider.hedera.wallet.exporting;

import com.hedera.hashgraph.sdk.HederaKeystore;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.exporting.KeyExporter;
import org.springframework.stereotype.Component;

@Component
public class HederaKeystoreExporter implements KeyExporter<HederaSecretKey> {

    @Override
    public String export(HederaSecretKey key,
                         final String password) {
        try {
            return new HederaKeystore(key.getKey()).export(password);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.hedera")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }
    }

    @Override
    public HederaSecretKey reconstructKey(String keystore,
                                          String password) {
        try {
            return HederaSecretKey.builder().key(new HederaKeystore(keystore, password).getPrivateKey()).build();
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("export.hedera")
                                 .message("An error occurred while trying to export the key")
                                 .build();
        }

    }

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }
}
