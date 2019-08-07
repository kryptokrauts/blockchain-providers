package network.arkane.provider.tron.sign;

import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.tron.secret.generation.TronSecretKeyMother;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

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

    @Test
    void decodesHex() throws UnsupportedEncodingException {
        String message = "0x536f6d652064617461";

        HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result.getV()).isEqualTo("0x1b");
        assertThat(result.getR()).isEqualTo("0xb6a11e4c785d706b213d840c816cc20ff084f9e679b08969720dfdf1c56c5568");
        assertThat(result.getS()).isEqualTo("0x7a7763c49ece02b3cdeb182f9f6afc333f52488845144bda15067a6243a48167");
        assertThat(result.getSignature()).isEqualTo(
                "0xb6a11e4c785d706b213d840c816cc20ff084f9e679b08969720dfdf1c56c55687a7763c49ece02b3cdeb182f9f6afc333f52488845144bda15067a6243a481671b");
    }

    @Test
    void hexAndPlainTextReturnSameResult() {
        final String message = "Some data";
        final String messageAsHex = "0x" + Hex.encodeHexString(message.getBytes(Charset.forName("UTF-8")));

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKeyMother.aDeterministicSecretKey());
        final HexSignature hexResult = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(messageAsHex).build(), TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result).isEqualTo(hexResult);
    }

    @Test
    void canSignDataStartingWith0xWhichIsNotHex() {
        String message = "0xRacers";

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().build().builder().data(message).build(), TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result.getV()).isEqualTo("0x1c");
        assertThat(result.getR()).isEqualTo("0x5d2f594353637a09a1aa6cfdb74dce086a66b7cc0d706bcc90f75a6590287383");
        assertThat(result.getS()).isEqualTo("0x36653c52ebdf1bcdf5de52577d305bb68c27ed0599a39d096b948d194e867b5b");
        assertThat(result.getSignature()).isEqualTo(
                "0x5d2f594353637a09a1aa6cfdb74dce086a66b7cc0d706bcc90f75a659028738336653c52ebdf1bcdf5de52577d305bb68c27ed0599a39d096b948d194e867b5b1c");
    }

}