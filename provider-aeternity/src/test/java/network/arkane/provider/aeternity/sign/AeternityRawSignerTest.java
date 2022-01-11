package network.arkane.provider.aeternity.sign;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionService;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AeternityRawSignerTest {

  private AeternityRawSigner aeternityRawSigner;
  private AeternityService aeternityService;
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    this.transactionService = mock(TransactionService.class);
    this.aeternityService = mock(AeternityService.class);
    this.aeternityService.transactions = transactionService;
    this.aeternityRawSigner = new AeternityRawSigner(this.aeternityService);
  }

  @Test
  void signTransaction() throws Exception {

    final String unsignedTransaction = "tx_+IUrAaEBV1+B/7Cil7dyXcZx2gsXabH8XL5FOFx7WtH8Lq8dYJ0LoQXc8QU36IYbbsXk7d7Lg77BPQuicjS136jJKX5wHepi9QOHAZu6brCYAAAAgicQhDuaygCqKxFM1wuWG58AoFFwNxylSmNg4Pv8OlwzrrPdOBQ95X6DOW+5H6nRMbqY3bEntQ==";

    final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    final KeyPair keyPair = keyPairService.generateKeyPair();

    final String exptectedMock = "expected";
    when(aeternityService.transactions
        .signTransaction(eq(unsignedTransaction), eq(keyPair.getEncodedPrivateKey())))
        .thenReturn(exptectedMock);

    TransactionSignature signature = (TransactionSignature) aeternityRawSigner.createSignature(
        AeternityRawSignable.builder()
            .data(unsignedTransaction)
            .build(), AeternitySecretKey.builder().keyPair(keyPair).build());
    assertThat(signature.getSignedTransaction()).isNotEmpty();
  }
}