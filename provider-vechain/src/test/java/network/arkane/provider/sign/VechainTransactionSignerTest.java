package network.arkane.provider.sign;

import network.arkane.provider.wallet.decryption.VechainWalletDecryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class VechainTransactionSignerTest {

    private VechainTransactionSigner signer;

    @BeforeEach
    void setUp() {
        final VechainWalletDecryptor decryptor = mock(VechainWalletDecryptor.class);
        this.signer = new VechainTransactionSigner(decryptor);
    }

    @Test
    void correctType() {
        assertThat(signer.getType()).isEqualTo(VechainTransactionSignable.class);
    }
}