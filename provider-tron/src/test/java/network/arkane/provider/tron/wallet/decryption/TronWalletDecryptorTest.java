package network.arkane.provider.tron.wallet.decryption;

import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.secret.generation.TronSecretKeyMother;
import network.arkane.provider.tron.wallet.generation.GeneratedTronWallet;
import network.arkane.provider.tron.wallet.generation.TronWalletGenerator;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TronWalletDecryptorTest {

    private TronWalletDecryptor tronWalletDecryptor;

    @BeforeEach
    void setUp() {
        tronWalletDecryptor = new TronWalletDecryptor();
    }

    @Test
    void generateKey() {
        final String generatedPassword = "password";
        final GeneratedTronWallet generatedWallet = (GeneratedTronWallet) new TronWalletGenerator().generateWallet(generatedPassword, TronSecretKeyMother.aRandomSecretKey());
        final TronSecretKey tronSecretKey = tronWalletDecryptor.generateKey(generatedWallet, generatedPassword);
        assertThat(Hex.encodeHexString(tronSecretKey.getKeyPair().getAddress())).isNotEmpty();
    }
}