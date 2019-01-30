package network.arkane.provider.bitcoin.extraction;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BitcoinWifExtractorTest {

    private BitcoinWifExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new BitcoinWifExtractor(new BitcoinEnv(Network.BTC_TEST, TestNet3Params.get()));
    }

    @Test
    void extract() {
        String privateKey = "92Pg46rUhgTT7romnV7iGW6W1gbGdeezqdbJCzShkCsYNzyyNcc";
        BitcoinSecretKey result = extractor.extract(new BitcoinWifExtractionRequest(privateKey));

        assertThat(result.getKey().getPrivateKeyAsWiF(TestNet3Params.get())).isEqualTo(privateKey);
    }

}