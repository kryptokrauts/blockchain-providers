package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "ethereum.balance.strategy", havingValue = "native")
public class EthereumNativeBalanceStrategy extends EvmNativeBalanceStrategy {


    public EthereumNativeBalanceStrategy(EthereumWeb3JGateway web3JGateway,
                                         TokenDiscoveryService tokenDiscoveryService) {
        super(web3JGateway, tokenDiscoveryService);
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
