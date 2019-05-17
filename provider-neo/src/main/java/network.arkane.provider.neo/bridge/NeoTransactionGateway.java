package network.arkane.provider.neo.bridge;

import io.neow3j.crypto.Hash;
import io.neow3j.crypto.transaction.RawTransaction;
import io.neow3j.io.BinaryWriter;
import io.neow3j.io.NeoSerializableInterface;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.utils.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Service;


import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

import io.neow3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class NeoTransactionGateway implements TransactionGateway {

    private Neow3j web3j;

    public NeoTransactionGateway(Neow3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public SecretType getType() {
        return SecretType.NEO;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse) {
        try {
            log.debug("Sending transaction to Neo node {}", signTransactionResponse.getSignedTransaction());
            final NeoSendRawTransaction send = web3j.sendRawTransaction(signTransactionResponse.getSignedTransaction()).send();
            if (send.hasError()) {
                if (send.getError().getMessage().contains("Insufficient funds")) {
                    log.debug("Got error from Neo chain: insufficient funds");
                    throw arkaneException()
                            .errorCode("transaction.insufficient-funds")
                            .message("The account that initiated the transfer does not have enough energy")
                            .build();
                } else {
                    log.debug("Got error from Neo chain: {}", send.getError().getMessage());
                    throw arkaneException()
                            .errorCode("transaction.submit.Neo-error")
                            .message(send.getError().getMessage())
                            .build();
                }
            } else {
                log.debug("Updating last nonce");
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
    private String getTxId(String signedTransaction) throws IllegalAccessException, InstantiationException, IOException {
        RawTransaction rawTransaction = NeoSerializableInterface.from(Numeric.hexStringToByteArray(signedTransaction), RawTransaction.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryWriter binaryWriter = new BinaryWriter(outputStream);
        rawTransaction.getScripts().clear();
        rawTransaction.serialize(binaryWriter);
        binaryWriter.flush();
        byte[] unsignedTx = outputStream.toByteArray();
        byte[] data_bytes = Hash.sha256(Hash.sha256(unsignedTx));
        return Numeric.toHexString(ArrayUtils.reverseArray(data_bytes));
    }

}
