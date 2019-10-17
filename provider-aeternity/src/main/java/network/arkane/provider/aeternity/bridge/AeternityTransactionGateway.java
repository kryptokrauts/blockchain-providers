package network.arkane.provider.aeternity.bridge;

import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import com.kryptokrauts.aeternity.sdk.service.transaction.domain.PostTransactionResult;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AeternityTransactionGateway implements TransactionGateway {

    private AeternityService aeternityService;

    public AeternityTransactionGateway(final @Qualifier("aeternity-service") AeternityService aeternityService) {
        this.aeternityService = aeternityService;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, final Optional<String> endpoint) {
        log.debug("Sending transaction to aeternity node {}", transactionSignature.getSignedTransaction());
        try {
            PostTransactionResult postTransactionResult = aeternityService.transactions.blockingPostTransaction(transactionSignature.getSignedTransaction());
            return new SubmittedAndSignedTransactionSignature(postTransactionResult.getTxHash(), transactionSignature.getSignedTransaction());
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
