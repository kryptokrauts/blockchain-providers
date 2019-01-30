package network.arkane.provider.sign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VechainTransactionSignerTest {

    private VechainTransactionSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new VechainTransactionSigner();
    }

    @Test
    void correctType() {
        assertThat(signer.getType()).isEqualTo(VechainTransactionSignable.class);
    }
}