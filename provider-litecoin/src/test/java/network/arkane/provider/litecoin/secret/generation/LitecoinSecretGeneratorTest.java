package network.arkane.provider.litecoin.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LitecoinSecretGeneratorTest {

    private LitecoinSecretGenerator secretGenerator;

    @BeforeEach
    void setUp() {
        secretGenerator = new LitecoinSecretGenerator();
    }


    @Test
    void returnsType() {
        SecretType result = secretGenerator.type();

        assertThat(result).isEqualTo(SecretType.LITECOIN);
    }

    @Test
    void generatesSecret() {
        LitecoinSecretKey generate = secretGenerator.generate();

        assertThat(generate.getKey()).isNotNull();
    }
}