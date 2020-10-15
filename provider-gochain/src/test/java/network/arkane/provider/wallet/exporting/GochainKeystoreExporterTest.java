package network.arkane.provider.wallet.exporting;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.wallet.decryption.EvmWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class GochainKeystoreExporterTest {

    private GochainKeystoreExporter gochainKeystoreExporter;
    private EvmWalletDecryptor decyptor;


    @BeforeEach
    void setUp() {
        decyptor = mock(EvmWalletDecryptor.class);
        this.gochainKeystoreExporter = new GochainKeystoreExporter(decyptor);
    }

    @Test
    void exports() {
        final String export = gochainKeystoreExporter.export(EvmSecretKey.builder().type(SecretType.GOCHAIN).keyPair(ECKeyPair.create(BigInteger.ZERO)).build(), "test");

        final WalletFile exportedWallet = JSONUtil.fromJson(export, WalletFile.class);
        assertThat(exportedWallet.getAddress()).isEqualTo("3f17f1962b36e491b30a40b2405849e597ba5fb5");
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> gochainKeystoreExporter.export(EvmSecretKey.builder()
                                                                            .type(SecretType.GOCHAIN)
                                                                            .keyPair(null)
                                                                            .build(), "test")).hasMessageContaining("An error occurred while trying to export the key");
    }
}
