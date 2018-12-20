package network.arkane.provider.sign;

import network.arkane.provider.wallet.decryption.EthereumWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EthereumRawSignerTest {

    private EthereumRawSigner signer;
    private EthereumWalletDecryptor decyptor;

    @BeforeEach
    void setUp() {
        decyptor = mock(EthereumWalletDecryptor.class);
        this.signer = new EthereumRawSigner(decyptor);
    }

    @Test
    void name() {
        assertThat(signer.getType()).isEqualTo(EthereumRawSignable.class);
    }
}