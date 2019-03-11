package network.arkane.provider.tron.secret.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TronSecretGeneratorTest {

    private TronSecretGenerator tronSecretGenerator;

    @BeforeEach
    void setUp() {
        tronSecretGenerator = new TronSecretGenerator();
    }

    @Test
    void generate() {
        TronSecretKey generate = tronSecretGenerator.generate();
        assertThat(generate.getKeyPair().getAddress()).isNotEmpty();
    }
}