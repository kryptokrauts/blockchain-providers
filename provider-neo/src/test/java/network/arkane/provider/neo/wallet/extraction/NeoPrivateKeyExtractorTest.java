package network.arkane.provider.neo.wallet.extraction;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class NeoPrivateKeyExtractorTest {

    private NeoPrivateKeyExtractor extractor;

    @BeforeEach
    public void setUp() {
        extractor = new NeoPrivateKeyExtractor();
    }

    @Test
    public void extract() {
        final SecretKey extract = extractor.extract(new NeoPrivateKeyExtractionRequest("90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28"));
        assertThat(extract.type()).isEqualTo(SecretType.NEO);
        assertThat(((NeoSecretKey) extract).getKey().getPrivateKey().toString(16)).isEqualTo("90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28");
    }

    @Test
    public void extractWrongPrivateKey() {
        assertThatThrownBy(() -> extractor.extract(new NeoPrivateKeyExtractionRequest("90765a22db47a27042be069adb-55768a958119d6063286a1d7ed28")))
                .hasMessageContaining("Unable to decode Neo private key 90765a22db47a27042be069adb-55768a958119d6063286a1d7ed28");
    }

    @Test
    public void extractAndSanitize() {
        final SecretKey extract = extractor.extract(new NeoPrivateKeyExtractionRequest("0x90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28"));
        assertThat(extract.type()).isEqualTo(SecretType.NEO);
        assertThat(((NeoSecretKey) extract).getKey().getPrivateKey().toString(16)).isEqualTo("90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28");
    }
}