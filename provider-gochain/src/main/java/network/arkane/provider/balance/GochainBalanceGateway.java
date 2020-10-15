package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.GochainWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GochainBalanceGateway extends EvmNativeBalanceGateway {


    public GochainBalanceGateway(final GochainWeb3JGateway web3JGateway,
                                 final TokenDiscoveryService tokenDiscoveryService) {
        super(web3JGateway, tokenDiscoveryService);
    }

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }

}
