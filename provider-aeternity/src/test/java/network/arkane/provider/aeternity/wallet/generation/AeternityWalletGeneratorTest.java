package network.arkane.provider.aeternity.wallet.generation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AeternityWalletGeneratorTest {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    private AeternityWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new AeternityWalletGenerator();
    }

    @Test
    public void generateWallet() {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new AeternitySecretKey(keyPairService.generateKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedAeternityWallet.class);
        assertThat(generatedWallet.getAddress()).isNotEmpty();

        String keystoreJson = ((GeneratedAeternityWallet) generatedWallet).getKeystoreJson();
        assertThat(keystoreJson).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new AeternitySecretKey(keyPairService.generateKeyPair()))).hasMessage("Password should not be empty");
    }
}