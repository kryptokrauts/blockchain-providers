package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.GochainSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GochainWalletGeneratorTest {

    private GochainWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new GochainWalletGenerator();
    }

    @Test
    public void generateWallet() throws Exception {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new GochainSecretKey(Keys.createEcKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedGochainWallet.class);

        assertThat(generatedWallet.getAddress()).isNotEmpty();

        WalletFile walletFile = ((GeneratedGochainWallet) generatedWallet).getWalletFile();
        assertThat(walletFile).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new GochainSecretKey(Keys.createEcKeyPair()))).hasMessage("Password should not be empty")
        ;
    }
}