package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;

import java.util.Optional;


@Slf4j
public abstract class BlockchainProviderTransactionGateway implements TransactionGateway {

    private final BlockchainProviderGatewayClient blockchainProviderGatewayClient;
    private final SecretType secretType;

    protected BlockchainProviderTransactionGateway(BlockchainProviderGatewayClient blockchainProviderGatewayClient,
                                                   SecretType secretType) {
        this.blockchainProviderGatewayClient = blockchainProviderGatewayClient;
        this.secretType = secretType;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, Optional<String> endpoint) {
        try {
            return blockchainProviderGatewayClient.post("/api/transactions", transactionSignature, Signature.class);
        } catch (Exception ex) {
            log.error("Error trying to submit a signed transaction: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                    .errorCode("transaction.submit.internal-error")
                    .message("A problem occurred trying to submit the transaction to the " + getType().name() + " network")
                    .cause(ex)
                    .build();
        }
    }

    @Override
    public SecretType getType() {
        return secretType;
    }
}
