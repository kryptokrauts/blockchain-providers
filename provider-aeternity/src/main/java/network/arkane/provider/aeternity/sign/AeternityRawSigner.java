package network.arkane.provider.aeternity.sign;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

import com.kryptokrauts.aeternity.sdk.domain.secret.KeyPair;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AeternityRawSigner implements Signer<AeternityRawSignable, AeternitySecretKey> {

  private AeternityService aeternityService;

  public AeternityRawSigner(
      final @Qualifier("aeternity-service") AeternityService aeternityService) {
    this.aeternityService = aeternityService;
  }

  @Override
  public Signature createSignature(AeternityRawSignable signable, AeternitySecretKey key) {
    try {
      KeyPair keyPair = key.getKeyPair();
      final String signedTx = aeternityService.transactions
          .signTransaction(signable.getData(), keyPair.getEncodedPrivateKey());
      return TransactionSignature.signTransactionBuilder().signedTransaction(signedTx).build();
    } catch (Exception ex) {
      log.error("Error trying to sign aeternity transaction", ex);
      throw arkaneException()
          .errorCode("A problem occurred trying to sign the aeternity transaction")
          .cause(ex)
          .build();
    }
  }
}
