package network.arkane.provider.aeternity.wallet.extraction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.aeternity.wallet.extraction.request.AeternityPrivateKeyExtractionRequest;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AeternityPrivateKeyExtractorTest {

    private static final String PRIVATE_KEY = "4d881dd1917036cc231f9881a0db978c8899dd76a817252418606b02bf6ab9d22378f892b7cc82c2d2739e994ec9953aa36461f1eb5a4a49a5b0de17b3d23ae8";
    private static final String ADDRESS = "ak_Gd6iMVsoonGuTF8LeswwDDN2NF5wYHAoTRtzwdEcfS32LWoxm";

    private AeternityPrivateKeyExtractor extractor;

    @BeforeEach
    public void setUp() {
        extractor = new AeternityPrivateKeyExtractor();
    }

    @Test
    public void extract() {
        final SecretKey extract = extractor.extract(new AeternityPrivateKeyExtractionRequest(PRIVATE_KEY));
        AssertionsForClassTypes.assertThat(SecretType.AETERNITY).isEqualTo(extract.type());
        assertThat(ADDRESS).isEqualTo(((AeternitySecretKey) extract).getKeyPair().getAddress());
    }

    @Test
    public void extractWrongPrivateKey() {
        assertThatThrownBy(() -> extractor.extract(new AeternityPrivateKeyExtractionRequest("invalid" + PRIVATE_KEY)))
                .hasMessageContaining("Unable to decode aeternity private key");
    }
}