package io.venly.provider.imx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.BlockchainProviderNonFungibleGateway;

public class ImxNonFungibleGateway extends BlockchainProviderNonFungibleGateway {

    public ImxNonFungibleGateway(ImxGatewayClient imxGatewayClient) {
        super(SecretType.IMX, imxGatewayClient);
    }

}
