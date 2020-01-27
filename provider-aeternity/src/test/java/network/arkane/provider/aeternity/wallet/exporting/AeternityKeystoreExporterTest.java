package network.arkane.provider.aeternity.wallet.exporting;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.decryption.AeternityWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AeternityKeystoreExporterTest {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();

    private AeternityKeystoreExporter aeternityKeystoreExporter;
    private AeternityWalletDecryptor aeternityWalletDecryptor;
    private RawKeyPair rawKeyPair;

    @BeforeEach
    void setUp() {
        aeternityWalletDecryptor = new AeternityWalletDecryptor();
        aeternityKeystoreExporter = new AeternityKeystoreExporter(aeternityWalletDecryptor);
        rawKeyPair = keyPairService.generateRawKeyPair();
    }

    @Test
    void exports() {
        final String export = aeternityKeystoreExporter.export(AeternitySecretKey.builder().keyPair(rawKeyPair).build(), "test");
        final RawKeyPair reconstructKeyPair = aeternityKeystoreExporter.reconstructKey(export, "test").getKeyPair();
        assertThat(rawKeyPair).isEqualTo(reconstructKeyPair);
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> aeternityKeystoreExporter.export(AeternitySecretKey.builder()
                                                                                  .keyPair(null)
                                                                                  .build(), "test")).hasMessageContaining("An error occurred while trying to export the key");
    }
}