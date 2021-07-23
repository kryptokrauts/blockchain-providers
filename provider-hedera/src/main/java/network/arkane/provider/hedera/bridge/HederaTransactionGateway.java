package network.arkane.provider.hedera.bridge;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Component
public class HederaTransactionGateway implements TransactionGateway {

    private final Client hederaClient;

    public HederaTransactionGateway(Client hederaClient) {
        this.hederaClient = hederaClient;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature,
                            Optional<String> endpoint) {
        Transaction<?> signedTransferTxn = null;
        try {
            signedTransferTxn = Transaction.fromBytes(Base64.decodeBase64(transactionSignature.getSignedTransaction()));
            TransactionResponse transactionResponse = signedTransferTxn.execute(hederaClient);
            return signAndSubmitTransactionBuilder()
                    .transactionHash(transactionResponse.transactionId.toString())
                    .signedTransaction(transactionSignature.getSignedTransaction())
                    .build();
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("error.hedera.submit")
                    .message("Error submitting signed hedera transaction")
                    .cause(e)
                    .build();
        }
    }

    @Override
    public SecretType getType() {
        return SecretType.HEDERA;
    }
}
