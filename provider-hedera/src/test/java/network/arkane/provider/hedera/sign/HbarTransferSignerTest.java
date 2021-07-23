package network.arkane.provider.hedera.sign;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.sign.domain.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class HbarTransferSignerTest {

    private HbarTransferSigner signer;

    @BeforeEach
    void setUp() {
        signer = new HbarTransferSigner(HederaTestFixtures.testClient());
    }

    @Test
    void sign() {
        Signature signature = signer.createSignature(HbarTransferSignable.builder()
                                                                         .from(HederaTestFixtures.getAccountId().toString())
                                                                         .to("0.0.2151494")
                                                                         .amount(new BigDecimal("0.01"))
                                                                         .build(),
                                                     HederaSecretKey.builder().key(HederaTestFixtures.getOperatorKey()).build());

        assertThat(signature).isNotNull();
    }
}
