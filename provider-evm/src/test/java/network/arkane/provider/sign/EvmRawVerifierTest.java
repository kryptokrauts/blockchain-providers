package network.arkane.provider.sign;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class EvmRawVerifierTest {
    private EvmRawVerifier verifier;
    private EvmRawSigner signer;

    @BeforeEach
    void setUp() {
        verifier = new EvmRawVerifier();
        signer = new EvmRawSigner();
    }

    @Test
    void validSignature() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String address = Keys.getAddress(aPair);

        String message = "Some data";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        boolean valid = verifier.isValidSignature(EvmRawVerifiable.builder().prefix(true).address(address).message(message).signature(result.getSignature()).build());

        assertThat(valid).isTrue();
    }

    @Test
    void validSignatureWithoutPrefix() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String address = Keys.getAddress(aPair);

        String message = "Some data";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().prefix(false).data(message).build(),
                                                     EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        boolean valid = verifier.isValidSignature(EvmRawVerifiable.builder().prefix(false).address(address).message(message).signature(result.getSignature()).build());

        assertThat(valid).isTrue();
    }

    @Test
    void notValidSignature_wrongSignature() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String address = Keys.getAddress(aPair);

        String message = "Some data";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        boolean valid = verifier.isValidSignature(EvmRawVerifiable.builder().prefix(true).address(address).message(message).signature("b" + result.getSignature()).build());

        assertThat(valid).isFalse();
    }

    @Test
    void notValidSignature_wrongAddress() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String address = "0x37e167766e4433fE6DeF90077443aD14376f29cb";

        String message = "Some data";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).build(), EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        boolean valid = verifier.isValidSignature(EvmRawVerifiable.builder().prefix(true).address(address).message(message).signature("b" + result.getSignature()).build());

        assertThat(valid).isFalse();
    }

    @Test
    void notValidSignature_prefixIssue() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String address = Keys.getAddress(aPair);

        String message = "Some data";

        HexSignature result = signer.createSignature(EvmRawSignable.builder().data(message).prefix(true).build(),
                                                     EvmSecretKey.builder().type(SecretType.ETHEREUM).keyPair(aPair).build());

        boolean valid = verifier.isValidSignature(EvmRawVerifiable.builder().prefix(false).address(address).message(message).signature(result.getSignature()).build());

        assertThat(valid).isFalse();
    }


}
