package network.arkane.provider.neo.secret;


import network.arkane.provider.neo.secret.generation.NeoSecretGenerator;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NeoSecretGeneratorTest {

    private NeoSecretGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new NeoSecretGenerator();
    }

    @Test
    void generate() {
        NeoSecretKey result = generator.generate();
        assertThat(result.getKey()).isNotNull();
    }


}