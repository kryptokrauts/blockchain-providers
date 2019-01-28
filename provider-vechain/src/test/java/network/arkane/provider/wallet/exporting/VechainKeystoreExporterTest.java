package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.secret.generation.VechainSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class VechainKeystoreExporterTest {

    private VechainKeystoreExporter vechainKeystoreExporter;

    @BeforeEach
    void setUp() {
        this.vechainKeystoreExporter = new VechainKeystoreExporter();
    }

    @Test
    void exports() {
        final String export = vechainKeystoreExporter.export(VechainSecretKey.builder().keyPair(ECKeyPair.create(BigInteger.ZERO)).build(), "test");

        final WalletFile exportedWallet = JSONUtil.fromJson(export, WalletFile.class);
        assertThat(exportedWallet.getAddress()).isEqualTo("3f17f1962b36e491b30a40b2405849e597ba5fb5");
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> vechainKeystoreExporter.export(
                VechainSecretKey.builder()
                                .keyPair(null)
                                .build(), "test")).hasMessageContaining("An error occurred while trying to export the key");
    }
}