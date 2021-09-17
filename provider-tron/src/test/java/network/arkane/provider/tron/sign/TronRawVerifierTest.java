package network.arkane.provider.tron.sign;

import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.tron.grpc.GrpcClient;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tron.common.crypto.ECKey;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TronRawVerifierTest {


    private TronRawVerifier verifier;
    private TronRawSigner signer;

    @BeforeEach
    void setUp() {
        verifier = new TronRawVerifier();
        signer = new TronRawSigner();
    }

    @Test
    void validSignature() throws UnsupportedEncodingException {

        ECKey ecKey = ECKey.fromPrivate(new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16));

        String address = GrpcClient.encode58Check(ecKey.getAddress());

        String message = "Some data";

        HexSignature result = signer.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKey.builder().keyPair(ecKey).build());

        boolean valid = verifier.isValidSignature(TronRawVerifiable.builder().address(address).message(message).signature(result.getSignature()).build());

        assertThat(valid).isTrue();
    }

    @Test
    void notValidSignature_wrongSignature() throws UnsupportedEncodingException {
        ECKey ecKey = ECKey.fromPrivate(new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16));

        String address = GrpcClient.encode58Check(ecKey.getAddress());

        String message = "Some data";

        HexSignature result = signer.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKey.builder().keyPair(ecKey).build());

        boolean valid = verifier.isValidSignature(TronRawVerifiable.builder().address(address).message(message).signature("b" + result.getSignature()).build());

        assertThat(valid).isFalse();
    }

    @Test
    void notValidSignature_wrongAddress() throws UnsupportedEncodingException {
        ECKey ecKey = ECKey.fromPrivate(new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16));

        String address = GrpcClient.encode58Check(ecKey.getAddress());

        String message = "Some data";

        HexSignature result = signer.createSignature(TronRawSignable.builder().data(message).build(), TronSecretKey.builder().keyPair(ecKey).build());

        boolean valid = verifier.isValidSignature(TronRawVerifiable.builder().address(address).message(message).signature("b" + result.getSignature()).build());

        assertThat(valid).isFalse();
    }

}
