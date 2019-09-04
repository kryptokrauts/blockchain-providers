package network.arkane.provider.sign;

import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class VechainRawSignerTest {

    private VechainRawSigner signer;

    @BeforeEach
    void setUp() {
        signer = new VechainRawSigner();
    }

    @Test
    void correctResult() throws UnsupportedEncodingException {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);

        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        String message = "Some data";

        HexSignature result = signer.createSignature(VechainRawSignable.builder().data(message).build(), VechainSecretKey.builder().keyPair(aPair).build());

        assertThat(result.getSignature()).isEqualTo(
                "0x340a74d159d3cecc5309818e4a3c14136e708e2e19fd432db073c8489bc0d7927fe399fcb1f6f62f10576556fb7044de48bded662655524158dde63070340a161b");
    }

}