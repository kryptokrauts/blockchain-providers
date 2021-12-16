package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "azrael")
public class MaticAzraelBalanceStrategy extends EvmAzraelBalanceStrategy {


    public MaticAzraelBalanceStrategy(MaticWeb3JGateway web3JGateway,
                                      AzraelClient erc20MaticAzraelClient,
                                      TokenDiscoveryProperties tokenDiscoveryProperties) {
        super(web3JGateway, erc20MaticAzraelClient, tokenDiscoveryProperties);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
