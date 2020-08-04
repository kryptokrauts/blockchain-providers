package network.arkane.provider.aeternity.sign;

import com.kryptokrauts.aeternity.sdk.domain.StringResultWrapper;
import com.kryptokrauts.aeternity.sdk.domain.secret.impl.BaseKeyPair;
import com.kryptokrauts.aeternity.sdk.exception.TransactionCreateException;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import com.kryptokrauts.aeternity.sdk.service.transaction.type.model.SpendTransactionModel;
import com.kryptokrauts.aeternity.sdk.util.EncodingUtils;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AeternitySpendTransactionSigner implements
    Signer<AeternitySpendTransactionSignable, AeternitySecretKey> {

  private AeternityService aeternityService;

  public AeternitySpendTransactionSigner(
      final @Qualifier("aeternity-service") AeternityService aeternityService) {
    this.aeternityService = aeternityService;
  }

  @Override
  public Signature createSignature(final AeternitySpendTransactionSignable signable,
      final AeternitySecretKey key) {
    SpendTransactionModel spendTransactionModel = SpendTransactionModel.builder()
        .amount(signable.getAmount())
        .sender(signable.getSender())
        .recipient(signable.getRecipient())
        .payload(signable.getPayload())
        .ttl(signable.getTtl())
        .nonce(signable.getNonce())
        // the fee is optional because the SDK can calculate it automatically
        .fee(signable.getFee())
        .build();
    StringResultWrapper unsignedTx = aeternityService.transactions
        .blockingCreateUnsignedTransaction(spendTransactionModel);
    BaseKeyPair baseKeyPair = EncodingUtils.createBaseKeyPair(key.getKeyPair());
    try {
      String signedTx = aeternityService.transactions
          .signTransaction(unsignedTx.getResult(), baseKeyPair.getPrivateKey());
      return TransactionSignature.signTransactionBuilder().signedTransaction(signedTx).build();
    } catch (TransactionCreateException e) {
      log.error("Unable to sign transaction: {}", e.getMessage());
      throw ArkaneException.arkaneException()
          .errorCode("A problem occurred trying to sign the aeternity transaction")
          .cause(e)
          .build();
    }
  }
}
