package network.arkane.provider.litecoin.extraction;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Keys;

import static org.assertj.core.api.Assertions.assertThat;

class LitecoinWifExtractorTest {

    LitecoinWifExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new LitecoinWifExtractor();
    }

    @Test
    void hasCorrectInputTypeRequest() {
        Class<LitecoinWifExtractionRequest> result = extractor.getImportRequestType();

        assertThat(result).isEqualTo(LitecoinWifExtractionRequest.class);
    }

    @Test
    void extractsSecret() throws Exception {
        ECKey privateKey = ECKey.fromPrivate(Keys.createEcKeyPair().getPrivateKey());
        String privateKeyAsWiF = privateKey.getPrivateKeyAsWiF(new LitecoinParams());

        LitecoinSecretKey result = extractor.extract(new LitecoinWifExtractionRequest(privateKeyAsWiF));

        assertThat(result.getKey()).isEqualTo(privateKey);
        assertThat(result.type()).isEqualTo(SecretType.LITECOIN);
    }
}