package network.arkane.provider.sign;

import network.arkane.provider.secret.generation.EthereumSecretKey;
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

class EthereumRawSignerTest {

    private EthereumRawSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new EthereumRawSigner();
    }

    @Test
    void name() {
        assertThat(signer.getType()).isEqualTo(EthereumRawSignable.class);
    }

    @Test
    void canSignWithoutPrefixAndWithoutHashing() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";
        String message = input;
        final Sign.SignatureData signatureData = Sign.signMessage(message.getBytes(StandardCharsets.UTF_8), aPair, false);

        HexSignature result = signer.createSignature(EthereumRawSignable.builder().data(input).prefix(false).hash(false).build(), EthereumSecretKey.builder().keyPair(aPair).build());

        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo(result.getR());
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo(result.getS());
        assertThat("0x" + Integer.toHexString(signatureData.getV())).isEqualTo(result.getV());
    }

    @Test
    void canSignWithoutPrefix() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";
        String message = input;
        final Sign.SignatureData signatureData = Sign.signMessage(message.getBytes(StandardCharsets.UTF_8), aPair);

        HexSignature result = signer.createSignature(EthereumRawSignable.builder().data(input).prefix(false).build(), EthereumSecretKey.builder().keyPair(aPair).build());

        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo(result.getR());
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo(result.getS());
        assertThat("0x" + Integer.toHexString(signatureData.getV())).isEqualTo(result.getV());
    }

    @Test
    void isSameAsWeb3Js() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "Some data";

        final Sign.SignatureData signatureData = Sign.signPrefixedMessage(message.getBytes(StandardCharsets.UTF_8), aPair);
        assertThat(Integer.toHexString(signatureData.getV())).isEqualTo("1c");
        assertThat(Numeric.toHexString(signatureData.getR())).isEqualTo("0xb91467e570a6466aa9e9876cbcd013baba02900b8979d43fe208a4a4f339f5fd");
        assertThat(Numeric.toHexString(signatureData.getS())).isEqualTo("0x6007e74cd82e037b800186422fc2da167c747ef045e5d18a5f5d4300f8e1a029");

        HexSignature result = signer.createSignature(EthereumRawSignable.builder().data("Some data").build(), EthereumSecretKey.builder().keyPair(aPair).build());

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

        HexSignature result = signer.createSignature(EthereumRawSignable.builder().data(message).build(), EthereumSecretKey.builder().keyPair(aPair).build());

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

        HexSignature result = signer.createSignature(EthereumRawSignable.builder().data(message).build(), EthereumSecretKey.builder().keyPair(aPair).build());
        HexSignature hexResult = signer.createSignature(EthereumRawSignable.builder().data(messageAsHex).build(), EthereumSecretKey.builder().keyPair(aPair).build());

        assertThat(result).isEqualTo(hexResult);
    }


}