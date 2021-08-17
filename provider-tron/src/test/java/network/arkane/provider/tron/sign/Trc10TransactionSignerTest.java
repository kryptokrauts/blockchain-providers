package network.arkane.provider.tron.sign;

import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static network.arkane.provider.tron.secret.generation.TronSecretKeyMother.aRandomSecretKey;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Trc10TransactionSignerTest {

    private Trc10TransactionSigner signer;

    @BeforeEach
    void setUp() {
        signer = new Trc10TransactionSigner();
    }

    @Test
    void createSignature() {
        assertThatThrownBy(() -> signer.createSignature(Trc10TransactionSignable.builder()
                                                                                .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                                                                .token("1002000")
                                                                                .amount(1000L)
                                                                                .build(), aRandomSecretKey())).isInstanceOf(
                ChainNoLongerSupportedException.class);
    }
}
