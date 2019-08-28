package network.arkane.provider.neo.wallet.generation;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.wallet.nep6.NEP6Wallet;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NeoWalletGeneratorTest {

    private NeoWalletGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new NeoWalletGenerator();
    }

    @Test
    public void generateWallet() throws Exception {
        final GeneratedWallet generatedWallet = generator.generateWallet("password", new NeoSecretKey(ECKeyPair.createEcKeyPair()));
        assertThat(generatedWallet).isInstanceOf(GeneratedNeoWallet.class);

        assertThat(generatedWallet.getAddress()).isNotEmpty();

        NEP6Wallet walletFile = ((GeneratedNeoWallet) generatedWallet).getWalletFile();
        assertThat(walletFile).isNotNull();
    }

    @Test
    public void passwordShouldNotBeEmpty() {
        assertThatThrownBy(() -> generator.generateWallet("", new NeoSecretKey(ECKeyPair.createEcKeyPair()))).hasMessage("Password should not be empty")
        ;
    }
}