package network.arkane.provider.bsc.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.EvmNativeBalanceGateway;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BscBalanceGateway extends EvmNativeBalanceGateway {

    public BscBalanceGateway(final BscWeb3JGateway web3JGateway,
                             final TokenDiscoveryService tokenDiscoveryService) {
        super(web3JGateway, tokenDiscoveryService);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }


}
