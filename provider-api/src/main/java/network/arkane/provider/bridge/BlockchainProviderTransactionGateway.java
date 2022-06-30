package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.imx.ImxTransactionInfo;

import java.util.Optional;

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
        return blockchainProviderGatewayClient.post("/api/transactions", transactionSignature, Signature.class);
    }

    public TxInfo getStatusInfo(String txId) {
        return blockchainProviderGatewayClient.get("/api/transactions/{txHash}/status", ImxTransactionInfo.class, txId);
    }

    @Override
    public SecretType getType() {
        return secretType;
    }
}
