package network.arkane.provider.wallet.extraction;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.PrivateKeyExtractionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class EthereumPrivateKeyExtractorTest {

    private EthereumPrivateKeyExtractor extractor;

    @BeforeEach
    public void setUp() {
        extractor = new EthereumPrivateKeyExtractor();
    }

    @Test
    public void extract() {
        final SecretKey extract = extractor.extract(new PrivateKeyExtractionRequest(SecretType.ETHEREUM, "90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28"));
        assertThat(extract.type()).isEqualTo(SecretType.ETHEREUM);
        assertThat(((EthereumSecretKey) extract).getKeyPair().getPrivateKey().toString(16)).isEqualTo("90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28");
    }

    @Test
    public void extractWrongPrivateKey() {
        assertThatThrownBy(() -> extractor.extract(new PrivateKeyExtractionRequest(SecretType.ETHEREUM, "90765a22db47a27042be069adb-55768a958119d6063286a1d7ed28")))
                .hasMessageContaining("Unable to decode ethereum private key");
    }

    @Test
    public void extractAndSanitize() {
        final SecretKey extract = extractor.extract(new PrivateKeyExtractionRequest(SecretType.ETHEREUM, "0x90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28"));
        assertThat(extract.type()).isEqualTo(SecretType.ETHEREUM);
        assertThat(((EthereumSecretKey) extract).getKeyPair().getPrivateKey().toString(16)).isEqualTo("90765a22db47a27042be069adb38635da9fc55768a958119d6063286a1d7ed28");
    }
}