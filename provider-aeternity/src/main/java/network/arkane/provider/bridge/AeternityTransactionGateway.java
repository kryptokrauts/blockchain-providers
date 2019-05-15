package network.arkane.provider.bridge;

import com.kryptokrauts.aeternity.generated.model.PostTxResponse;
import com.kryptokrauts.aeternity.generated.model.Tx;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionService;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AeternityTransactionGateway implements TransactionGateway {

    private TransactionService transactionService;

    public AeternityTransactionGateway(final @Qualifier("aeternity-transactionService") TransactionService transactionService) { this.transactionService = transactionService; }

    @Override
    public Signature submit(TransactionSignature transactionSignature) {
        Tx tx = new Tx();
        tx.setTx(transactionSignature.getSignedTransaction());
        log.debug("Sending transaction to aeternity node {}", transactionSignature.getSignedTransaction());
        try {
            PostTxResponse postTxResponse = transactionService.postTransaction(tx).blockingGet();
            return new SubmittedAndSignedTransactionSignature(postTxResponse.getTxHash(), transactionSignature.getSignedTransaction());
        } catch (RuntimeException e) {
            throw ArkaneException.arkaneException()
                    .errorCode("transaction.submit.aeternity-error")
                    .cause(e)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Override
    public SecretType getType() {
        return SecretType.AETERNITY;
    }
}
