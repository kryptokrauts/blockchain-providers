package network.arkane.provider.bitcoin.extraction;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BitcoinWifPassphraseExtractorTest {

    private BitcoinWifPassphraseExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new BitcoinWifPassphraseExtractor(new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get()));
    }

    @Test
    void extract() {
        final String password = "password";
        final String wif = "92Pg46rUhgTT7romnV7iGW6W1gbGdeezqdbJCzShkCsYNzyyNcc";
        final String encryptedWif = "6PRMCKW1QPMxLhu6RkVFvQ9dG7d2mgxParv6DxhiMyTJ98UBPJDsK3k6Wp";
        final BitcoinSecretKey result = extractor.extract(new BitcoinWifPassphraseExtractionRequest(encryptedWif, password));

        assertThat(result.getKey().getPrivateKeyAsWiF(TestNet3Params.get())).isEqualTo(wif);
    }

}