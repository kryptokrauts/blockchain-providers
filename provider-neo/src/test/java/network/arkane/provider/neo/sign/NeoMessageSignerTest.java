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
        assertThat(signer.getType()).isEqualTo(NeoMessageSigner.class);
    }

    @Test
    void canSignWithoutHashing() {
        BigInteger privateKeyInBT = new BigInteger("4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f362318", 16);
        ECKeyPair aPair = ECKeyPair.create(privateKeyInBT);

        final String input = "Some data";

        HexSignature signatureData = (HexSignature) signer.createSignature(NeoSignableMessage.builder().data(input).hash(false).build(), NeoSecretKey.builder().key(aPair).build());

        assertThat(signatureData.getR()).isEqualTo("qsdf");
        assertThat(signatureData.getS()).isEqualTo("fdsqs");
        assertThat(signatureData.getV()).isEqualTo("0x1c");
    }

}