package network.arkane.provider.hedera.wallet;

import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class HederaWalletGeneratorTest {

    private HederaWalletGenerator hederaWalletGenerator;

    @BeforeEach
    void setUp() {
        hederaWalletGenerator = new HederaWalletGenerator(HederaTestFixtures.clientFactory());
    }

    @Test
    void generateWallet() {
        GeneratedWallet generatedWallet = hederaWalletGenerator.generateWallet("password", HederaSecretKey.builder().key(PrivateKey.generate()).build());
    }
}
