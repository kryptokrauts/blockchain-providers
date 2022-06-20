package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;

import java.util.Optional;

public abstract class TransactionProviderGateway<Response extends Signature> implements TransactionGateway {

    private final BlockchainProviderGatewayClient blockchainProviderGatewayClient;
    private final SecretType secretType;
    private final Class<Response> responseClass;

    protected TransactionProviderGateway(BlockchainProviderGatewayClient blockchainProviderGatewayClient,
                                         SecretType secretType,
                                         Class<Response> responseClass) {
        this.blockchainProviderGatewayClient = blockchainProviderGatewayClient;
        this.secretType = secretType;
        this.responseClass = responseClass;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, Optional<String> endpoint) {
        return blockchainProviderGatewayClient.post("/api/transactions", transactionSignature, responseClass);
    }

    @Override
    public SecretType getType() {
        return secretType;
    }
}
