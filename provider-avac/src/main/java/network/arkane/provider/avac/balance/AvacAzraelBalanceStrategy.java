package network.arkane.provider.avac.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.balance.EvmAzraelBalanceStrategy;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "avac.balance.strategy", havingValue = "azrael")
public class AvacAzraelBalanceStrategy extends EvmAzraelBalanceStrategy {


    public AvacAzraelBalanceStrategy(AvacWeb3JGateway web3JGateway,
                                     AzraelClient avacAzraelClient,
                                     TokenDiscoveryProperties tokenDiscoveryProperties) {
        super(web3JGateway, avacAzraelClient, tokenDiscoveryProperties);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
