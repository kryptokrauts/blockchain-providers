package io.venly.provider.imx;

import network.arkane.provider.bridge.BlockchainProviderTransactionGateway;
import network.arkane.provider.chain.SecretType;

public class ImxTransactionGateway extends BlockchainProviderTransactionGateway {

    public ImxTransactionGateway(final ImxGatewayClient imxGatewayClient) {
        super(imxGatewayClient, SecretType.IMX);
    }
}
