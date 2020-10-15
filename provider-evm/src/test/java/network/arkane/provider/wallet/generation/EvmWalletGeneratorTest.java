package network.arkane.provider.wallet.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EvmSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EvmWalletGeneratorTest {

    private EvmWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new EvmWalletGenerator();
    }

    @Test
    public void generateWallet() throws Exception {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new EvmSecretKey(SecretType.GOCHAIN, Keys.createEcKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedEvmWallet.class);

        assertThat(generatedWallet.getAddress()).isNotEmpty();

        WalletFile walletFile = ((GeneratedEvmWallet) generatedWallet).getWalletFile();
        assertThat(walletFile).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new EvmSecretKey(SecretType.GOCHAIN, Keys.createEcKeyPair()))).hasMessage("Password should not be empty");
    }

}
