package network.arkane.provider.neo.extraction;

import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NeoWifExtractorTest {

    private NeoWifExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new NeoWifExtractor();
    }

    @Test
    void extract() {
        String privateKey = "Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf";
        NeoSecretKey result = extractor.extract(new NeoWifExtractionRequest(privateKey));

        assertThat(result.getKey().exportAsWIF()).isEqualTo(privateKey);
    }
}