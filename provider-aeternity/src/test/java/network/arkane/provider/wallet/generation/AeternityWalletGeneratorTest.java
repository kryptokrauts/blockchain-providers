package network.arkane.provider.wallet.generation;

import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AeternityWalletGeneratorTest {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    private AeternityWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new AeternityWalletGenerator();
    }

    @Test
    public void generateWallet() throws Exception {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new AeternitySecretKey(keyPairService.generateRawKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedAeternityWallet.class);
        assertThat(generatedWallet.getAddress()).isNotEmpty();

        String keystoreJson = ((GeneratedAeternityWallet) generatedWallet).getKeystoreJson();
        assertThat(keystoreJson).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new AeternitySecretKey(keyPairService.generateRawKeyPair()))).hasMessage("Password should not be empty");
    }
}