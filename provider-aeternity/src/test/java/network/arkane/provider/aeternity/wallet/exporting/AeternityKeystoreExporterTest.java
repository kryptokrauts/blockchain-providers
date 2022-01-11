package network.arkane.provider.aeternity.wallet.exporting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.decryption.AeternityWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AeternityKeystoreExporterTest {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();

    private AeternityKeystoreExporter aeternityKeystoreExporter;
    private AeternityWalletDecryptor aeternityWalletDecryptor;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() {
        aeternityWalletDecryptor = new AeternityWalletDecryptor();
        aeternityKeystoreExporter = new AeternityKeystoreExporter(aeternityWalletDecryptor);
        keyPair = keyPairService.generateKeyPair();
    }

    @Test
    void exports() {
        final String export = aeternityKeystoreExporter.export(AeternitySecretKey.builder().keyPair(keyPair).build(), "test");
        final KeyPair reconstructKeyPair = aeternityKeystoreExporter.reconstructKey(export, "test").getKeyPair();
        assertThat(keyPair).isEqualTo(reconstructKeyPair);
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> aeternityKeystoreExporter.export(AeternitySecretKey.builder()
                                                                                  .keyPair(null)
                                                                                  .build(), "test")).hasMessageContaining("An error occurred while trying to export the key");
    }
}