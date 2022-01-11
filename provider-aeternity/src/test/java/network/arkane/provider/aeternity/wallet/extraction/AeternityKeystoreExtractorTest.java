package network.arkane.provider.aeternity.wallet.extraction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.extraction.request.AeternityKeystoreExtractionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AeternityKeystoreExtractorTest {

    private static final String KEYSTORE = "{\r\n\t\"public_key\": \"ak_2hSFmdK98bhUw4ar7MUdTRzNQuMJfBFQYxdhN9kaiopDGqj3Cr\",\r\n\t\"crypto\": {\r\n\t\t\"secret_type\": \"ed25519\",\r\n\t\t\"symmetric_alg\": \"xsalsa20-poly1305\",\r\n\t\t\"ciphertext\": \"71acf8412331806b3ad5482cda1f6e682c541c522f61715056faf5ed2d21a9c4d68fe2cdcf147b1be99fbab20b33433f82b2a2d3bbc772957bd2fb6cf9a97f611670e0f044d8076efbe31fe30142e6f5\",\r\n\t\t\"cipher_params\": {\r\n\t\t\t\"nonce\": \"375673a887fd10910fe3bfa9c9abfd72d3d240d124ed3f3b\"\r\n\t\t},\r\n\t\t\"kdf\": \"argon2id\",\r\n\t\t\"kdf_params\": {\r\n\t\t\t\"memlimit_kib\": 65536,\r\n\t\t\t\"opslimit\": 2,\r\n\t\t\t\"salt\": \"a84887d9539cbd9490d2b1a5c41262b2\",\r\n\t\t\t\"parallelism\": 1\r\n\t\t}\r\n\t},\r\n\t\"id\": \"ff7d9f9c-e0ab-4fab-b8fc-beab2d322f6b\",\r\n\t\"name\": \"!!!FOR TESTING PURPOSE ONLY!!! - Wed 14 Nov 2018 09:40:10 CET - password:aeternity\",\r\n\t\"version\": 1\r\n}";

    private AeternityKeystoreExtractor extractor;

    @BeforeEach
    public void setUp() throws Exception {
        extractor = new AeternityKeystoreExtractor();
    }

    @Test
    public void extract() {
        final AeternitySecretKey password = (AeternitySecretKey) extractor.extract(new AeternityKeystoreExtractionRequest(KEYSTORE, "aeternity"));
        assertThat(password).isNotNull();
        assertThat(password.getKeyPair().getEncodedPrivateKey()).isNotNull();
        assertThat(password.getKeyPair().getAddress()).isNotNull();

    }

    @Test
    public void extractKeystoreWrong() {
        final String keystore = "blah";
        assertThatThrownBy(() -> extractor.extract(new AeternityKeystoreExtractionRequest(keystore, "TestTest123")))
                .hasMessageContaining("Not a valid keystore file");
    }

    @Test
    void extractPasswordWrong() {
        assertThatThrownBy(() -> extractor.extract(new AeternityKeystoreExtractionRequest(KEYSTORE, "blah")))
                .hasMessageContaining("Wrong password provided for given keystore file");
    }
}