package network.arkane.provider.tron.wallet.exporting;

import network.arkane.provider.tron.secret.generation.TronSecretKeyMother;
import network.arkane.provider.tron.wallet.decryption.TronWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class TronKeystoreExporterTest {

    private TronKeystoreExporter tronKeystoreExporter;
    private TronWalletDecryptor tronWalletDecryptor;

    @BeforeEach
    void setUp() {
        this.tronWalletDecryptor = mock(TronWalletDecryptor.class);
        tronKeystoreExporter = new TronKeystoreExporter(tronWalletDecryptor);
    }

    @Test
    void export() {
        assertThat(tronKeystoreExporter.export(TronSecretKeyMother.aRandomSecretKey(), "password")).isNotEmpty();
    }

    @Test
    void exportWithoutPasswordImpossible() {
        assertThatThrownBy(() -> tronKeystoreExporter.export(TronSecretKeyMother.aRandomSecretKey(), null))
                .hasMessageContaining("An error occurred while trying to export the tron-key");
    }
}