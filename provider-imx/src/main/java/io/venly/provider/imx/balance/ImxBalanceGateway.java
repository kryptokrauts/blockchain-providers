package io.venly.provider.imx.balance;

import io.venly.provider.imx.config.ImxProperties;
import network.arkane.provider.balance.BlockchainProviderBalanceGateway;
import network.arkane.provider.balance.BasicAuthCredentials;
import network.arkane.provider.chain.SecretType;

public class ImxBalanceGateway extends BlockchainProviderBalanceGateway {

    public ImxBalanceGateway(ImxProperties imxProperties) {
        super(SecretType.IMX,
              imxProperties.endpoint(),
              new BasicAuthCredentials(
                      imxProperties.user(),
                      imxProperties.password())
             );
    }

}
