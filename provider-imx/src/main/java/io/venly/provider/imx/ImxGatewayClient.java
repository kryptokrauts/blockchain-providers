package io.venly.provider.imx;

import network.arkane.provider.client.BasicAuthCredentials;
import network.arkane.provider.client.BlockchainProviderGatewayClient;

class ImxGatewayClient extends BlockchainProviderGatewayClient {

    ImxGatewayClient(ImxProperties imxProperties) {
        super(imxProperties.endpoint(),
              new BasicAuthCredentials(imxProperties.user(), imxProperties.password()));
    }
}
