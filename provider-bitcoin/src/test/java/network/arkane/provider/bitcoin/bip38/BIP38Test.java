package network.arkane.provider.bitcoin.bip38;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BIP38Test {

    private BIP38 bip38;

    @BeforeEach
    void setUp() {
        this.bip38 = new BIP38(new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get()));
    }

    @Test
    void createBIP38() {
        DumpedPrivateKey pk = DumpedPrivateKey.fromBase58(TestNet3Params.get(),
                                                          "92JYtSuKyhrG1fVgtBXUQgT8yNGs6XFFCjz1XLCwg8jFM95GHB6");
        String testnetWif = pk.getKey().getPrivateKeyAsWiF(TestNet3Params.get());
        System.out.println("testnet wif: " + testnetWif);
        String generatedKey = bip38.encryptNoEC("test", testnetWif, false);
        System.out.println("generated key: " + generatedKey);
        System.out.println("decrypted value: " + bip38.decrypt("test", generatedKey));
    }
}