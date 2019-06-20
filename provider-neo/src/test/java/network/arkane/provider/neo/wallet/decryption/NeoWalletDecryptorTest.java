package network.arkane.provider.neo.wallet.decryption;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.WIF;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.neo.wallet.generation.NeoWalletGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NeoWalletDecryptorTest {

    private NeoWalletGenerator generator;
    private NeoSecretKey key;
    private  NeoWalletDecryptor decryptor;

    @BeforeEach
    void setUp() {
        ECKeyPair ecKeyPair = ECKeyPair.create(WIF.getPrivateKeyFromWIF("Kx9xMQVipBYAAjSxYEoZVatdVQfhYHbMFWSYPinSgAVd1d4Qgbpf"));
        key = NeoSecretKey.builder().key(ecKeyPair).build();
        generator = new NeoWalletGenerator();
        decryptor = new NeoWalletDecryptor();
    }

    @Test
    void generateKey() {
        final GeneratedNeoWallet generatedWallet = (GeneratedNeoWallet) generator.generateWallet("password", key);
        final NeoSecretKey decKey = decryptor.generateKey(generatedWallet,"password");

        assertThat(key.getKey().exportAsWIF()).isEqualTo(decKey.getKey().exportAsWIF());

    }
}