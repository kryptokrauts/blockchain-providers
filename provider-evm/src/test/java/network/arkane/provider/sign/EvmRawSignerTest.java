package network.arkane.provider.sign;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class EvmRawSignerTest {

    private EvmRawSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new EvmRawSigner();
    }

    @Test
    void name() {
        assertThat(signer.getType()).isEqualTo(EvmRawSignable.class);
    }

    @Test
    void canSignWithoutPrefixAndWithoutHashing() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";
        String message = input;
        final Sign.SignatureData signatureData = Sign.signMessage(message.getBytes(StandardCharsets.UTF_8), aPair, false);

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(input).prefix(false).hash(false).build(),
                                                     EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo(result.getR());
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo(result.getS());
        assertThat(Numeric.toHexString(signatureData.getV())).isEqualTo(result.getV());
    }

    @Test
    void canSignWithoutPrefix() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";
        final Sign.SignatureData signatureData = Sign.signMessage(input.getBytes(StandardCharsets.UTF_8), aPair);

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(input).prefix(false).build(),
                                                     EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo(result.getR());
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo(result.getS());
        assertThat(Numeric.toHexString(signatureData.getV())).isEqualTo(result.getV());
    }

    @Test
    void isSameAsWeb3Js() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "Some data";

        final Sign.SignatureData signatureData = Sign.signPrefixedMessage(message.getBytes(StandardCharsets.UTF_8), aPair);
        assertThat(Numeric.toHexString(signatureData.getV())).isEqualTo("0x1c");
        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo("0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd");
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo("0x6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a029");

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data("Some data").build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(result.getV()).isEqualTo("0x1c");
        assertThat(result.getR()).isEqualTo("0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd");
        assertThat(result.getS()).isEqualTo("0x6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a029");
        assertThat(result.getSignature()).isEqualTo(
                "0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a0291c");
    }

    @Test
    void decodesHex() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "0x536f6d652064617461";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(result.getV()).isEqualTo("0x1c");
        assertThat(result.getR()).isEqualTo("0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd");
        assertThat(result.getS()).isEqualTo("0x6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a029");
        assertThat(result.getSignature()).isEqualTo(
                "0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a0291c");
    }

    @Test
    void hexAndPlainTextReturnSameResult() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "Some data";
        String messageAsHex = "0x" + Hex.encodeHexString(message.getBytes(Charset.forName("UTF-8")));

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());
        HexSignature hexResult = signer.createSignature(EvmRawSignable.builder().data(messageAsHex).build(),
                                                        EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(result).isEqualTo(hexResult);
    }

    @Test
    void canSignDataStartingWith0xWhichIsNotHex() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "0xRacers";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        assertThat(result.getV()).isEqualTo("0x1c");
        assertThat(result.getR()).isEqualTo("0x3de0d6ed6097fc48e51d98432e093119ae1ec55b3a8a98c5a089fe4f02c8347e");
        assertThat(result.getS()).isEqualTo("0x4b2faf7668218e9f91c592a6278be7ac09fb0ea89977b361e2bf1826b0fda65c");
        assertThat(result.getSignature()).isEqualTo(
                "0x3de0d6ed6097fc48e51d98432e093119ae1ec55b3a8a98c5a089fe4f02c8347e4b2faf7668218e9f91c592a6278be7ac09fb0ea89977b361e2bf1826b0fda65c1c");
    }

}
