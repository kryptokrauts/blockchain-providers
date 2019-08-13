package network.arkane.provider.tron.sign;

import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.secret.generation.TronSecretKeyMother;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.common.crypto.ECKey;

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
        assertThat(((HexSignature) signature).getR()).isEqualTo("0x55aa8cc8090fe787259b6b80ae186ff9fda432c4706731ab7ed3a94fb30b7b28");
        assertThat(((HexSignature) signature).getS()).isEqualTo("0x5c7e288027014b9f764c3d4214b8f1fbdb68858b289a6ca260595069ab0a22a7");
        assertThat(((HexSignature) signature).getV()).isEqualTo("0x1c");
    }

    @Test
    void signUtf8() {
        Signature signature = tronRawSigner.createSignature(TronRawSignable.builder().data("A").build(), TronSecretKeyMother.aDeterministicSecretKey());
        assertThat(signature).isInstanceOf(HexSignature.class);
        assertThat(((HexSignature) signature).getR()).isEqualTo("0x55aa8cc8090fe787259b6b80ae186ff9fda432c4706731ab7ed3a94fb30b7b28");
        assertThat(((HexSignature) signature).getS()).isEqualTo("0x5c7e288027014b9f764c3d4214b8f1fbdb68858b289a6ca260595069ab0a22a7");
        assertThat(((HexSignature) signature).getV()).isEqualTo("0x1c");
    }

    @Test
    void decodesHex() throws UnsupportedEncodingException {
        String message = "0x536f6d652064617461";

        HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result.getV()).isEqualTo("0x1b");
        assertThat(result.getR()).isEqualTo("0x1ec8fe7fea5054722c7c3976a1c040d219361a173addb6910964908155e9615e");
        assertThat(result.getS()).isEqualTo("0x69dc4cad1525bb3785aec4039be87cebe807b028fc00c037c47b86ee67c5aa83");
        assertThat(result.getSignature()).isEqualTo(
                "0x1ec8fe7fea5054722c7c3976a1c040d219361a173addb6910964908155e9615e69dc4cad1525bb3785aec4039be87cebe807b028fc00c037c47b86ee67c5aa831b");
    }

    @Test
    void hexAndPlainTextReturnSameResult() {
        final String message = "Some data";
        final String messageAsHex = "0x" + Hex.encodeHexString(message.getBytes(Charset.forName("UTF-8")));

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKeyMother.aDeterministicSecretKey());
        final HexSignature hexResult = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(messageAsHex).build(),
                                                                                    TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result).isEqualTo(hexResult);
    }

    @Test
    void canSignDataStartingWith0xWhichIsNotHex() {
        String message = "0xRacers";

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().build().builder().data(message).build(),
                                                                                 TronSecretKeyMother.aDeterministicSecretKey());

        assertThat(result.getV()).isEqualTo("0x1c");
        assertThat(result.getR()).isEqualTo("0x7bdf182a49b1508d45b9c18edf2fedc3a35bed27639f6a28d587cfbb6061681f");
        assertThat(result.getS()).isEqualTo("0x50adafec95d6a2995a1f9612644da3ed796565bba1387e618ba71536849569c1");
        assertThat(result.getSignature()).isEqualTo(
                "0x7bdf182a49b1508d45b9c18edf2fedc3a35bed27639f6a28d587cfbb6061681f50adafec95d6a2995a1f9612644da3ed796565bba1387e618ba71536849569c11c");
    }

    @Test
    void resultIsSameAsTronWeb() {
        String message = "hello";
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKey ecKey = ECKey.fromPrivate(privateKeyInBT);

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(),
                                                                                 TronSecretKey.builder().keyPair(ecKey).build());

        assertThat(result.getSignature()).isEqualTo(
                "0xad769dd2313250b7e2651a099c8cd6440bfae939e7822edc050e2cf78863ab8f50f37cec86a9c3007891719191c0f1d94dfcf9cf4e97fbff5cf11414bec49c131b");
    }

    @Test
    void resultIsSameAsTronWebWith0xPrefix() {
        String message = "0xClient";
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKey ecKey = ECKey.fromPrivate(privateKeyInBT);

        final HexSignature result = (HexSignature) tronRawSigner.createSignature(TronRawSignable.builder().data(message).build(),
                                                                                 TronSecretKey.builder().keyPair(ecKey).build());

        assertThat(result.getSignature()).isEqualTo(
                "0x570ec03e853562bbd810d4d7d50bcbea966656033b457e764a84d662c968204c1acdc6b923bfb40dce5d0cf1cceee91922d264558aad1b3af4951c7b942c00391b");
    }
}