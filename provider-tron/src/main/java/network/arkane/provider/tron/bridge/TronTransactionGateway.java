package network.arkane.provider.tron.bridge;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tron.grpc.GrpcClient;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.tron.common.utils.Sha256Hash;
import org.tron.protos.Protocol;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
@Slf4j
public class TronTransactionGateway implements TransactionGateway {

    private GrpcClient grpcClient;

    public TronTransactionGateway(GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    @Override
    @SneakyThrows
    public Signature submit(final TransactionSignature transactionSignature,
                            final Optional<String> endpoint) {
        try {
            if (broadcast(Hex.decode(transactionSignature.getSignedTransaction()))) {
                return SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder()
                                                             .transactionHash(Sha256Hash.of(true,
                                                                                            Protocol.Transaction.parseFrom(Hex.decode(transactionSignature.getSignedTransaction()))
                                                                                                                .getRawData()
                                                                                                                .toByteArray()).toString())
                                                             .signedTransaction(transactionSignature.getSignedTransaction())
                                                             .build();
            } else {
                log.error("Unable to submit a signed transaction: {}", transactionSignature.getSignedTransaction());
                throw arkaneException()
                        .errorCode("transaction.submit.internal-error")
                        .message("A problem occurred trying to submit the transaction to the Tron network")
                        .build();
            }
        } catch (final Exception exception) {
            log.error("Error trying to submit a signed transaction: {}", exception.getMessage());
            throw arkaneException()
                    .errorCode("transaction.submit.internal-error")
                    .message("A problem occurred trying to submit the transaction to the Tron network")
                    .cause(exception)
                    .build();
        }
    }

    @SneakyThrows
    private boolean broadcast(byte[] transactionBytes) {
        final Protocol.Transaction transaction = Protocol.Transaction.parseFrom(transactionBytes);
        return grpcClient.broadcastTransaction(transaction);
    }

    @Override
    public SecretType getType() {
        return SecretType.TRON;
    }
}
