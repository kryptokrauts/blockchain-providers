package network.arkane.provider.neo.wallet.extraction;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WIF;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.decryption.NeoWalletDecryptor;
import network.arkane.provider.neo.wallet.exporting.NeoKeystoreExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class NeoKeystoreExtractorTest {
    private NeoKeystoreExtractor extractor;
    private NeoKeystoreExporter exporter;

    @BeforeEach
    void setUp() {
        extractor = new NeoKeystoreExtractor();
        exporter = new NeoKeystoreExporter(mock(NeoWalletDecryptor.class));
    }

    @Test
    void extract() {

        ECKeyPair keyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        final String keystore = exporter.export(NeoSecretKey.builder().key(keyPair).build(), "TestTest123");
        final NeoSecretKey password = (NeoSecretKey) extractor.extract(new NeoKeystoreExtractionRequest(keystore, "TestTest123"));
        assertThat(password).isNotNull();
        assertThat(password.getKey().getPrivateKey()).isNotNull();
        assertThat(password.getKey().getPublicKey()).isNotNull();

    }

    @Test
    void extractKeystoreWrong() {
        final String keystore = "blah";
        assertThatThrownBy(() -> extractor.extract(new NeoKeystoreExtractionRequest(keystore, "TestTest123")))
                .hasMessageContaining("Not a valid keystore file or Invalid password");
    }

    @Test
    void extractKeystoreWrongJson() {
        final String keystore = "{\"version\":3,\"id\":\"62550A99-FD80-4B4B-A6A6-CBE9EF0A65AB\","
                + "\"crypto\":{\"ciphertext\":\"b2ac326e75a0aa63ad03a4cd2bedc1f17a873764eb6122cd6be3258a321f1a12\","
                + "\"cipherparams\":{\"iv\":\"7b3a3bd3de595f9f6e91c4cd3d726121\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":8,\"p\":1,\"n\":262144,\"dklen\":32,"
                + "\"salt\":\"c3c9f2b87b3caaf502a2e9571b7e5b517ae6805e6d029b1ef42380b24f4700d4\"},"
                + "\"cipher\":\"aes-128-ctr\"},"
                + "\"address\":\"937bbac40da751ff4c72297dd377cd4da3ac1aee\"}";
        assertThatThrownBy(() -> extractor.extract(new NeoKeystoreExtractionRequest(keystore, "TestTest123")))
                .hasMessageContaining("Not a valid keystore file or Invalid password");
    }

    @Test
    void extractPasswordWrong() {
        ECKeyPair keyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        final String keystore = exporter.export(NeoSecretKey.builder().key(keyPair).build(), "test");
        assertThatThrownBy(() -> extractor.extract(new NeoKeystoreExtractionRequest(keystore, "blah")))
                .hasMessageContaining("Not a valid keystore file or Invalid password");
    }
}