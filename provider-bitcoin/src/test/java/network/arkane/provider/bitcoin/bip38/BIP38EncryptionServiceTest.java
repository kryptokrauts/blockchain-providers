package network.arkane.provider.bitcoin.bip38;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BIP38EncryptionServiceTest {

    private BIP38EncryptionService bip38EncryptionService;

    private BitcoinEnv bitcoinEnv;
    private BIP38 bip38;

    @BeforeEach
    void setUp() {
        bitcoinEnv = new BitcoinEnv(Network.BTC, TestNet3Params.get());
        bip38 = mock(BIP38.class);
        this.bip38EncryptionService = new BIP38EncryptionService(bitcoinEnv, bip38);
    }

    @Test
    void encrypt() {
        final String privateKey = "92JYtSuKyhrG1fVgtBXUQgT8yNGs6XFFCjz1XLCwg8jFM95GHB6";
        final DumpedPrivateKey key = DumpedPrivateKey.fromBase58(TestNet3Params.get(),
                                                                 privateKey);
        when(bip38.encryptNoEC(eq("passphrase"), eq(privateKey), eq(false))).thenReturn("encrypted");
        bip38EncryptionService.encrypt(BitcoinSecretKey.builder().key(key.getKey()).build(), "passphrase");
    }
}