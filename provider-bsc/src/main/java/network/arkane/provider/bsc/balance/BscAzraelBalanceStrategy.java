package network.arkane.provider.bsc.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.balance.EvmAzraelBalanceStrategy;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "bsc.balance.strategy", havingValue = "azrael")
public class BscAzraelBalanceStrategy extends EvmAzraelBalanceStrategy {


    public BscAzraelBalanceStrategy(BscWeb3JGateway web3JGateway,
                                    AzraelClient bscAzraelClient,
                                    TokenDiscoveryProperties tokenDiscoveryProperties) {
        super(web3JGateway, bscAzraelClient, tokenDiscoveryProperties);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
