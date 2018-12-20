package network.arkane.provider.sign;

import network.arkane.provider.wallet.decryption.EthereumWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EthereumTransactionSignerTest {

    private EthereumTransactionSigner signer;

    @BeforeEach
    void setUp() {
        final EthereumWalletDecryptor decryptor = mock(EthereumWalletDecryptor.class);
        this.signer = new EthereumTransactionSigner(decryptor);
    }

    @Test
    void correctType() {
        assertThat(signer.getType()).isEqualTo(EthereumTransactionSignable.class);
    }
}