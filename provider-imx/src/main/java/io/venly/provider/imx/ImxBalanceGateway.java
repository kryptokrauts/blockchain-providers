package io.venly.provider.imx;

import network.arkane.provider.balance.BlockchainProviderBalanceGateway;
import network.arkane.provider.chain.SecretType;

public class ImxBalanceGateway extends BlockchainProviderBalanceGateway {

    public ImxBalanceGateway(ImxGatewayClient imxGatewayClient) {
        super(SecretType.IMX, imxGatewayClient);
    }

}
