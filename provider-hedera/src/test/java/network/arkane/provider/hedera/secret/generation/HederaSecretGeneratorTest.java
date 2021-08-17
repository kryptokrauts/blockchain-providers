package network.arkane.provider.hedera.secret.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HederaSecretGeneratorTest {

    private HederaSecretGenerator hederaSecretGenerator;

    @BeforeEach
    void setUp() {
        hederaSecretGenerator = new HederaSecretGenerator();
    }

    @Test
    void generatesNewkey() {
        HederaSecretKey key = hederaSecretGenerator.generate();

        assertThat(key.getKey()).isNotNull();
    }
}
