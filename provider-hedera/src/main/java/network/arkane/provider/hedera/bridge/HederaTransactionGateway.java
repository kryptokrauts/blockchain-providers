package network.arkane.provider.hedera.bridge;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.tx.HederaTransactionReceipt;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Slf4j
@Component
public class HederaTransactionGateway implements TransactionGateway {

    private final Client hederaClient;

    public HederaTransactionGateway(HederaClientFactory clientFactory) {
        this.hederaClient = clientFactory.getClientWithOperator();
    }

    @Override
    public SubmittedAndSignedTransactionSignature submit(TransactionSignature transactionSignature,
                                                         Optional<String> endpoint) {
        Transaction<?> signedTransferTxn = null;
        try {
            signedTransferTxn = Transaction.fromBytes(Base64.decodeBase64(transactionSignature.getSignedTransaction()));
            TransactionResponse transactionResponse = signedTransferTxn.execute(hederaClient);
            return signAndSubmitTransactionBuilder()
                    .transactionHash(transactionResponse.transactionId.toString())
                    .signedTransaction(transactionSignature.getSignedTransaction())
                    .transactionDetails(this.getTransactionDetails(signedTransferTxn, transactionResponse))
                    .build();
        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("error.hedera.submit")
                    .message("Error submitting signed hedera transaction")
                    .cause(e)
                    .build();
        }
    }

    private Object getTransactionDetails(final Transaction<?> signedTransferTxn,
                                         final TransactionResponse transactionResponse) {
        if (signedTransferTxn instanceof TokenMintTransaction) {
            return this.mapHederaTransactionReceipt(transactionResponse);
        }
        return null;
    }


    private HederaTransactionReceipt mapHederaTransactionReceipt(final TransactionResponse transactionResponse) {
        try {
            final TransactionReceipt receipt = transactionResponse.getReceipt(hederaClient);
            return HederaTransactionReceipt.builder()
                                           .status(receipt.status.toString())
                                           .accountId(this.entityIdToString(receipt.accountId))
                                           .fileId(this.entityIdToString(receipt.fileId))
                                           .contractId(this.entityIdToString(receipt.contractId))
                                           .topicId(this.entityIdToString(receipt.topicId))
                                           .tokenId(this.entityIdToString(receipt.tokenId))
                                           .topicSequenceNumber(receipt.topicSequenceNumber)
                                           .topicRunningHash(Optional.ofNullable(receipt.topicRunningHash)
                                                                     .map(bytes -> bytes.toString(Charset.defaultCharset()))
                                                                     .orElse(null))
                                           .totalSupply(receipt.totalSupply)
                                           .scheduleId(this.entityIdToString(receipt.scheduleId))
                                           .scheduledTransactionId(this.entityIdToString(receipt.scheduledTransactionId))
                                           .serials(receipt.serials)
                                           .build();
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            log.error("Error fetching transaction receipt: ", e);
        }
        return null;
    }

    private <T> String entityIdToString(T entityId) {
        return entityId != null ? entityId.toString() : null;
    }


    @Override
    public SecretType getType() {
        return SecretType.HEDERA;
    }
}
