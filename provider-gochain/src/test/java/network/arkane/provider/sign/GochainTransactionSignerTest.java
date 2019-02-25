package network.arkane.provider.sign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GochainTransactionSignerTest {

    private GochainTransactionSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new GochainTransactionSigner();
    }

    @Test
    void correctType() {
        assertThat(signer.getType()).isEqualTo(GochainTransactionSignable.class);
    }
}