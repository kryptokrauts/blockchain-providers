package network.arkane.provider.sign;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EthereumTransactionSignerTest {

    private EthereumTransactionSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new EthereumTransactionSigner();
    }

    @Test
    void correctType() {
        assertThat(signer.getType()).isEqualTo(EthereumTransactionSignable.class);
    }
}