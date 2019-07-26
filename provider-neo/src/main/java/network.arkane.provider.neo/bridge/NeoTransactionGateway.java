package network.arkane.provider.neo.bridge;

import io.neow3j.crypto.transaction.RawTransaction;
import io.neow3j.io.NeoSerializableInterface;
import io.neow3j.model.types.TransactionType;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.transaction.ClaimTransaction;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Service
@Slf4j
public class NeoTransactionGateway implements TransactionGateway {

    private NeoW3JGateway neow3j;

    public NeoTransactionGateway(NeoW3JGateway neow3j) {
        this.neow3j = neow3j;
    }

    @Override
    public SecretType getType() {
        return SecretType.NEO;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse, final Optional<String> endpoint) {
        try {
            final NeoSendRawTransaction send = neow3j.sendRawTransaction(signTransactionResponse.getSignedTransaction());
            if (send.hasError()) {
                log.debug("Got error from Neo chain: insufficient funds");
                throw arkaneException()
                        .errorCode("transaction.submit.neo-error")
                        .message(send.getError().getMessage())
                        .build();
            } else {
                return signAndSubmitTransactionBuilder()
                        .transactionHash(getTxId(signTransactionResponse.getSignedTransaction()))
                        .signedTransaction(signTransactionResponse.getSignedTransaction())
                        .build();
            }
        } catch (final ArkaneException ex) {
            log.debug("Exception submitting transaction", ex);
            throw ex;
        } catch (final Exception ex) {
            log.error("Error trying to submit a signed transaction: {}", ex.getMessage());
            throw arkaneException()
                    .errorCode("transaction.submit.internal-error")
                    .message("A problem occurred trying to submit the transaction to the Neo network")
                    .cause(ex)
                    .build();
        }
    }

    // Get TxId from a signedTransaction
    private String getTxId(String signedTransaction) throws IllegalAccessException, InstantiationException {
        byte[] tx = Numeric.hexStringToByteArray(signedTransaction);
        TransactionType transactionType = TransactionType.valueOf(tx[0]);

        RawTransaction rawTransaction = null;

        if (transactionType == TransactionType.CONTRACT_TRANSACTION) {
            rawTransaction = NeoSerializableInterface.from(tx, ContractTransaction.class);
        } else if (transactionType == TransactionType.CLAIM_TRANSACTION) {
            rawTransaction = NeoSerializableInterface.from(tx, ClaimTransaction.class);
        } else {
            rawTransaction = NeoSerializableInterface.from(tx, InvocationTransaction.class);
        }

        return rawTransaction.getTxId();
    }

}
