package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "ethereum.balance.strategy", havingValue = "azrael")
public class EthereumAzraelBalanceStrategy extends EvmAzraelBalanceStrategy {


    public EthereumAzraelBalanceStrategy(EthereumWeb3JGateway web3JGateway,
                                         AzraelClient ethereumAzraelClient,
                                         TokenDiscoveryProperties tokenDiscoveryProperties) {
        super(web3JGateway, ethereumAzraelClient, tokenDiscoveryProperties);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
