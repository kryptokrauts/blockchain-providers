package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.wallet.exporting.request.VechainKeyExportRequest;
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
        final String export = vechainKeystoreExporter.export(VechainKeyExportRequest.builder()
                                                                                    .password("test")
                                                                                    .secretKey(VechainSecretKey.builder().keyPair(ECKeyPair.create(BigInteger.ZERO)).build())
                                                                                    .build());

        final WalletFile exportedWallet = JSONUtil.fromJson(export, WalletFile.class);
        assertThat(exportedWallet.getAddress()).isEqualTo("3f17f1962b36e491b30a40b2405849e597ba5fb5");
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> vechainKeystoreExporter.export(VechainKeyExportRequest.builder()
                                                                                       .password("test")
                                                                                       .secretKey(VechainSecretKey.builder()
                                                                                                                  .keyPair(null)
                                                                                                                  .build())
                                                                                       .build())).hasMessageContaining("An error occurred while trying to export the key");
    }
}