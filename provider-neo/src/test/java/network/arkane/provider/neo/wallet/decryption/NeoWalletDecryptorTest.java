package network.arkane.provider.neo.wallet.decryption;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.Keys;
import io.neow3j.crypto.WIF;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionInput;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.crypto.transaction.RawVerificationScript;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.utils.Numeric;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.neo.sign.NeoTransactionSignable;
import network.arkane.provider.neo.sign.NeoTransactionSigner;
import network.arkane.provider.neo.wallet.generation.GeneratedNeoWallet;
import network.arkane.provider.neo.wallet.generation.NeoWalletGenerator;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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