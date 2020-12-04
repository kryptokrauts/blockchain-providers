package network.arkane.provider.balance.azrael;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.balance.EvmAzraelBalanceGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryProperties;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "azrael")
public class MaticAzraelBalanceGateway extends EvmAzraelBalanceGateway {

    public MaticAzraelBalanceGateway(final MaticWeb3JGateway web3JGateway,
                                     final TokenDiscoveryService tokenDiscoveryService,
                                     final AzraelClient maticAzraelClient,
                                     final TokenDiscoveryProperties tokenDiscoveryProperties) {
        super(web3JGateway, tokenDiscoveryService, maticAzraelClient, "https://img.arkane.network" + tokenDiscoveryProperties.getPaths().get(SecretType.MATIC) + "/logos/");
    }


    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
