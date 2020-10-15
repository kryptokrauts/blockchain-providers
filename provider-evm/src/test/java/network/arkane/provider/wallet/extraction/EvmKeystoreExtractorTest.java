package network.arkane.provider.wallet.extraction;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.wallet.extraction.request.EvmKeystoreExtractionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EvmKeystoreExtractorTest {
    private EvmKeystoreExtractor extractor;

    @BeforeEach
    public void setUp() throws Exception {
        extractor = new EvmKeystoreExtractor();
    }

    @Test
    public void extract() {
        final String keystore = "{\"version\":3,\"id\":\"62550A99-FD80-4B4B-A6A6-CBE9EF0A65AB\","
                                + "\"crypto\":{\"ciphertext\":\"b2ac326e75a0aa63ad03a4cd2bedc1f17a873764eb6122cd6be3258a321f1a12\","
                                + "\"cipherparams\":{\"iv\":\"7b3a3bd3de595f9f6e91c4cd3d726121\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":8,\"p\":1,\"n\":262144,\"dklen\":32,"
                                + "\"salt\":\"c3c9f2b87b3caaf502a2e9571b7e5b517ae6805e6d029b1ef42380b24f4700d4\"},"
                                + "\"mac\":\"001f3a9be5ef4035ff5f2370e8029e915efa23c00e822658ff2e560692ff0c99\",\"cipher\":\"aes-128-ctr\"},"
                                + "\"address\":\"937bbac40da751ff4c72297dd377cd4da3ac1aee\"}";
        final EvmSecretKey password = (EvmSecretKey) extractor.extract(new EvmKeystoreExtractionRequest(SecretType.ETHEREUM, keystore, "TestTest123"));
        assertThat(password).isNotNull();
        assertThat(password.getKeyPair().getPrivateKey()).isNotNull();
        assertThat(password.getKeyPair().getPublicKey()).isNotNull();

    }

    @Test
    public void extractKeystoreWrong() {
        final String keystore = "blah";
        assertThatThrownBy(() -> extractor.extract(new EvmKeystoreExtractionRequest(SecretType.ETHEREUM, keystore, "TestTest123")))
                .hasMessageContaining("Not a valid keystore file");
    }

    @Test
    void extractKeystoreWrongJson() {
        final String keystore = "{\"version\":3,\"id\":\"62550A99-FD80-4B4B-A6A6-CBE9EF0A65AB\","
                                + "\"crypto\":{\"ciphertext\":\"b2ac326e75a0aa63ad03a4cd2bedc1f17a873764eb6122cd6be3258a321f1a12\","
                                + "\"cipherparams\":{\"iv\":\"7b3a3bd3de595f9f6e91c4cd3d726121\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":8,\"p\":1,\"n\":262144,\"dklen\":32,"
                                + "\"salt\":\"c3c9f2b87b3caaf502a2e9571b7e5b517ae6805e6d029b1ef42380b24f4700d4\"},"
                                + "\"cipher\":\"aes-128-ctr\"},"
                                + "\"address\":\"937bbac40da751ff4c72297dd377cd4da3ac1aee\"}";
        assertThatThrownBy(() -> extractor.extract(new EvmKeystoreExtractionRequest(SecretType.ETHEREUM, keystore, "TestTest123")))
                .hasMessageContaining("Not a valid keystore file");
    }

    @Test
    void extractPasswordWrong() {
        final String keystore = "{\"version\":3,\"id\":\"62550A99-FD80-4B4B-A6A6-CBE9EF0A65AB\","
                                + "\"crypto\":{\"ciphertext\":\"b2ac326e75a0aa63ad03a4cd2bedc1f17a873764eb6122cd6be3258a321f1a12\","
                                + "\"cipherparams\":{\"iv\":\"7b3a3bd3de595f9f6e91c4cd3d726121\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"r\":8,\"p\":1,\"n\":262144,\"dklen\":32,"
                                + "\"salt\":\"c3c9f2b87b3caaf502a2e9571b7e5b517ae6805e6d029b1ef42380b24f4700d4\"},"
                                + "\"mac\":\"001f3a9be5ef4035ff5f2370e8029e915efa23c00e822658ff2e560692ff0c99\",\"cipher\":\"aes-128-ctr\"},"
                                + "\"address\":\"937bbac40da751ff4c72297dd377cd4da3ac1aee\"}";
        assertThatThrownBy(() -> extractor.extract(new EvmKeystoreExtractionRequest(SecretType.ETHEREUM, keystore, "blah")))
                .hasMessageContaining("Wrong password provided for given keystore file");
    }


}
