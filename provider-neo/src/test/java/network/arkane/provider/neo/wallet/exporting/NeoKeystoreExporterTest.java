package network.arkane.provider.neo.wallet.exporting;

import io.neow3j.crypto.WIF;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.decryption.NeoWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WalletFile;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class NeoKeystoreExporterTest {

    private NeoKeystoreExporter neoKeystoreExporter;
    private NeoWalletDecryptor decyptor;


    @BeforeEach
    void setUp() {
        decyptor = mock(NeoWalletDecryptor.class);
        this.neoKeystoreExporter = new NeoKeystoreExporter(decyptor);
    }

    @Test
    void exports() {
        ECKeyPair keyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        final String export = neoKeystoreExporter.export(NeoSecretKey.builder().key(keyPair).build(), "test");

        final WalletFile exportedWallet = JSONUtil.fromJson(export, WalletFile.class);
        assertThat(exportedWallet.getAccounts().get(0).getAddress()).isEqualTo("AH16QyTdjADiuQwbSdXpjBVm6THz5w7Bro");
    }

    @Test
    void exportButException() {
        assertThatThrownBy(() -> neoKeystoreExporter.export(NeoSecretKey.builder()
                .key(null)
                .build(), "test")).hasMessageContaining("An error occurred while trying to export the key");
    }
}