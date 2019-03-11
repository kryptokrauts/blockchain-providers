package network.arkane.provider.sign;

import com.kryptokrauts.aeternity.generated.model.Tx;
import com.kryptokrauts.aeternity.generated.model.UnsignedTx;
import com.kryptokrauts.aeternity.sdk.domain.secret.impl.BaseKeyPair;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionService;
import com.kryptokrauts.aeternity.sdk.util.EncodingUtils;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.bouncycastle.crypto.CryptoException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class AeternitySpendTransactionSigner implements Signer<AeternitySpendTransactionSignable, AeternitySecretKey> {

    private TransactionService transactionService;

    public AeternitySpendTransactionSigner(final @Qualifier("transactionService") TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Signature createSignature(final AeternitySpendTransactionSignable signable, final AeternitySecretKey key) {
        UnsignedTx unsignedTx = transactionService.createSpendTx( signable.getSender(), signable.getRecipient(), signable.getAmount(), signable.getPayload(), signable.getFee(), signable.getTtl(), signable.getNonce() ).blockingGet();
        BaseKeyPair baseKeyPair = EncodingUtils.createBaseKeyPair(key.getKeyPair());
        try {
            Tx tx = transactionService.signTransaction(unsignedTx, baseKeyPair.getPrivateKey());
            return TransactionSignature.signTransactionBuilder().signedTransaction(tx.getTx()).build();
        } catch ( CryptoException e ) {
            log.error("Unable to sign transaction: {}", e.getMessage());
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .errorCode("A problem occurred trying to sign the aeternity transaction")
                    .cause(e)
                    .build();
        }
    }
}
