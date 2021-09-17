package network.arkane.provider.tron.sign;

import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static network.arkane.provider.tron.secret.generation.TronSecretKeyMother.aRandomSecretKey;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TronTransactionSignerTest {

    private TronTransactionSigner tronTransactionSigner;

    @BeforeEach
    void setUp() {
        tronTransactionSigner = new TronTransactionSigner();
    }

    @Test
    void createSignature() {
        final TronTransactionSignable signable = TronTransactionSignable.builder()
                                                                        .data("0x")
                                                                        .amount(BigInteger.valueOf(1000))
                                                                        .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                                                        .build();
        assertThatThrownBy(() -> tronTransactionSigner.createSignature(signable, aRandomSecretKey())).isInstanceOf(ChainNoLongerSupportedException.class);
    }

    @Test
    void createTriggerContractSignature() {
        TronTransactionSignable signable = TronTransactionSignable.builder()
                                                                  .data("0x74657374")
                                                                  .amount(BigInteger.valueOf(1000))
                                                                  .to("TQ69Jy7jTM12MnqBQZNuaZJWY9nLszAheq")
                                                                  .build();
        assertThatThrownBy(() -> tronTransactionSigner.createSignature(signable, aRandomSecretKey())).isInstanceOf(ChainNoLongerSupportedException.class);
    }
}
