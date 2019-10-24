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
import org.springframework.stereotype.Component;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
@Slf4j
public class AeternityRawSigner implements Signer<AeternityRawSignable, AeternitySecretKey> {

    private TransactionService transactionService;

    public AeternityRawSigner(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Signature createSignature(AeternityRawSignable signable, AeternitySecretKey key) {
        try {
            UnsignedTx unsignedTx = new UnsignedTx();
            unsignedTx.setTx(signable.getData());
            BaseKeyPair baseKeyPair = EncodingUtils.createBaseKeyPair(key.getKeyPair());
            final Tx tx = transactionService.signTransaction(unsignedTx, baseKeyPair.getPrivateKey());
            return TransactionSignature.signTransactionBuilder().signedTransaction(tx.getTx()).build();
        } catch (Exception ex) {
            log.error("Error trying to sign aeternity transaction", ex);
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .errorCode("A problem occurred trying to sign the aeternity transaction")
                    .cause(ex)
                    .build();
        }
    }
}
