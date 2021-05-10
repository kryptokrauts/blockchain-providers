package network.arkane.provider.avac.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.balance.EvmNativeBalanceStrategy;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "avac.balance.strategy", havingValue = "native")
public class AvacNativeBalanceStrategy extends EvmNativeBalanceStrategy {


    public AvacNativeBalanceStrategy(AvacWeb3JGateway web3JGateway,
                                     TokenDiscoveryService tokenDiscoveryService) {
        super(web3JGateway, tokenDiscoveryService);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
