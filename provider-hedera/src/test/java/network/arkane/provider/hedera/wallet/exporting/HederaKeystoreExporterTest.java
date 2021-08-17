package network.arkane.provider.hedera.wallet.exporting;

import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HederaKeystoreExporterTest {

    private HederaKeystoreExporter exporter;

    @BeforeEach
    void setUp() {
        exporter = new HederaKeystoreExporter();
    }

    @Test
    void export() {
        String exported = exporter.export(HederaSecretKey.builder().key(PrivateKey.generate()).build(), "password");

        assertThat(exported).isNotEmpty();
    }

    @Test
    void restore() {
        String keystore = "{\"version\":2,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"kdf\":\"pbkdf2\",\"cipherparams\":{\"iv\":\"5abc0bc5fa6b5c3e6140c1fe7d155ae3\"},"
                          + "\"ciphertext\":\"8f8098458f75cc2615a77a181d085801fc31ee87a4ca85871c4242c6be75eedc\",\"kdfparams\":{\"dkLen\":32,"
                          + "\"salt\":\"f593408c46660aa03400b938614c6dad8dcb920a219de062bbe0b119c7d2beee\",\"c\":262144,\"prf\":\"hmac-sha256\"},"
                          + "\"mac\":\"5aecf046954f47e522ed68429738d6b00976db4b50b8cc8a115e5ce451c8fad11247e47ffe22cd8eeeeb3c5da5862284\"}}";

        HederaSecretKey secretKey = exporter.reconstructKey(keystore, "password");

        assertThat(secretKey.getKey()).isNotNull();
    }
}
