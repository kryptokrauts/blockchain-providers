package network.arkane.provider.tron.sign;

import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.tron.secret.generation.TronSecretKeyMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TronRawSignerTest {

    private TronRawSigner tronRawSigner;

    @BeforeEach
    void setUp() {
        tronRawSigner = new TronRawSigner();
    }

    @Test
    void signHex() {
        Signature signature = tronRawSigner.createSignature(TronRawSignable.builder().data("0x41").build(), TronSecretKeyMother.aDeterministicSecretKey());
        assertThat(signature).isInstanceOf(HexSignature.class);
        assertThat(((HexSignature) signature).getR()).isEqualTo("0x5810872b74eb0b2674ba47bb70617fd2777e4b1b8681b38a64f3c3502c5527f2");
        assertThat(((HexSignature) signature).getS()).isEqualTo("0x73df388f9978bcbb4bd6e720c76e4013870fe7b8f885ed9fad2b4a60030ef269");
        assertThat(((HexSignature) signature).getV()).isEqualTo("0x1b");
    }

    @Test
    void signUtf8() {
        Signature signature = tronRawSigner.createSignature(TronRawSignable.builder().data("A").build(), TronSecretKeyMother.aDeterministicSecretKey());
        assertThat(signature).isInstanceOf(HexSignature.class);
        assertThat(((HexSignature) signature).getR()).isEqualTo("0x5810872b74eb0b2674ba47bb70617fd2777e4b1b8681b38a64f3c3502c5527f2");
        assertThat(((HexSignature) signature).getS()).isEqualTo("0x73df388f9978bcbb4bd6e720c76e4013870fe7b8f885ed9fad2b4a60030ef269");
        assertThat(((HexSignature) signature).getV()).isEqualTo("0x1b");
    }
}