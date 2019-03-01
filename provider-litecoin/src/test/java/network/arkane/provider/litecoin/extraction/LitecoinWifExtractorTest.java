package network.arkane.provider.litecoin.extraction;

import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import org.bitcoinj.core.ECKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Keys;

import static org.assertj.core.api.Assertions.assertThat;

class LitecoinWifExtractorTest {

    LitecoinWifExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new LitecoinWifExtractor(
                new LitecoinEnv(Network.LITECOIN, new LitecoinParams())
        );
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

    @Test
    void name() {
        LitecoinSecretKey extract = extractor.extract(new LitecoinWifExtractionRequest("6vRpuhEHyygBx8ck6M3YhJNfUDE3ZdgxD84f7CxirAYudE2VLif"));
        assertThat(extract.getKey().getPrivateKeyAsHex()).isEqualTo("ac21953e05073ad3f31731073ad9ddec554bb2e763217f5deee903203c255537");
    }
}