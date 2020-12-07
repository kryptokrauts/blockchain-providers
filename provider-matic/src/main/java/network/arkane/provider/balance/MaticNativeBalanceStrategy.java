package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "native")
public class MaticNativeBalanceStrategy extends EvmNativeBalanceStrategy {


    public MaticNativeBalanceStrategy(MaticWeb3JGateway web3JGateway,
                                      TokenDiscoveryService tokenDiscoveryService) {
        super(web3JGateway, tokenDiscoveryService);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
