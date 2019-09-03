package network.arkane.provider.neo.sign;

import io.neow3j.crypto.ECKeyPair;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class NeoMessageSignerTest {

    private NeoMessageSigner signer;

    @BeforeEach
    void setUp() {
        this.signer = new NeoMessageSigner();
    }

    @Test
    void name() {
        assertThat(signer.getType()).isEqualTo(NeoSignableMessage.class);
    }

    @Test
    void canSignWithoutHashing() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";

        HexSignature signatureData = (HexSignature) signer.createSignature(NeoSignableMessage.builder().data(input).hash(false).build(), NeoSecretKey.builder().key(aPair).build());

        assertThat(signatureData.getR()).isEqualTo("0xc8ac068398f6539b29cff00dc15176a148f87210418521c0c7242ed088e139d0");
        assertThat(signatureData.getS()).isEqualTo("0x518c9376630df615b691db0e0633ed51a64e672f14c96476b6351491e5a341ce");
        assertThat(signatureData.getV()).isEqualTo("0x1c");
    }

}