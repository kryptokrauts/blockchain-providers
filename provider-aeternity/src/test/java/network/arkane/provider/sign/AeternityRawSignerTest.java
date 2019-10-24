package network.arkane.provider.sign;

import com.kryptokrauts.aeternity.generated.model.Tx;
import com.kryptokrauts.aeternity.generated.model.UnsignedTx;
import com.kryptokrauts.aeternity.sdk.domain.secret.impl.BaseKeyPair;
import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionService;
import com.kryptokrauts.aeternity.sdk.util.EncodingUtils;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AeternityRawSignerTest {

    private AeternityRawSigner aeternityRawSigner;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        this.transactionService = mock(TransactionService.class);
        this.aeternityRawSigner = new AeternityRawSigner(this.transactionService);
    }

    @Test
    void signTransaction() throws Exception {

        final String unsignedTransaction = "tx_+IUrAaEBV1+B/7Cil7dyXcZx2gsXabH8XL5FOFx7WtH8Lq8dYJ0LoQXc8QU36IYbbsXk7d7Lg77BPQuicjS136jJKX5wHepi9QOHAZu6brCYAAAAgicQhDuaygCqKxFM1wuWG58AoFFwNxylSmNg4Pv8OlwzrrPdOBQ95X6DOW+5H6nRMbqY3bEntQ==";

        final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
        final RawKeyPair rawKeyPair = keyPairService.generateRawKeyPair();
        BaseKeyPair baseKeyPair = EncodingUtils.createBaseKeyPair(rawKeyPair);

        final Tx exptectedMock = mock(Tx.class);
        when(exptectedMock.getTx()).thenReturn("expected");
        when(transactionService.signTransaction(eq(from(unsignedTransaction)), eq(baseKeyPair.getPrivateKey())))
                .thenReturn(exptectedMock);

        TransactionSignature signature = (TransactionSignature) aeternityRawSigner.createSignature(AeternityRawSignable.builder()
                .data(unsignedTransaction)
                .build(), AeternitySecretKey.builder().keyPair(rawKeyPair).build());
        assertThat(signature.getSignedTransaction()).isNotEmpty();
    }


    public UnsignedTx from(final String unsigned) {
        final UnsignedTx unsignedTx = new UnsignedTx();
        unsignedTx.setTx(unsigned);
        return unsignedTx;
    }
}