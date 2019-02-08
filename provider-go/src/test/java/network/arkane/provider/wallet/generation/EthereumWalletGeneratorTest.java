package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.EthereumSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EthereumWalletGeneratorTest {

    private EthereumWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new EthereumWalletGenerator();
    }

    @Test
    public void generateWallet() throws Exception {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new EthereumSecretKey(Keys.createEcKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedEthereumWallet.class);

        assertThat(generatedWallet.getAddress()).isNotEmpty();

        WalletFile walletFile = ((GeneratedEthereumWallet) generatedWallet).getWalletFile();
        assertThat(walletFile).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new EthereumSecretKey(Keys.createEcKeyPair()))).hasMessage("Password should not be empty")
        ;
    }
}