package network.arkane.provider.bsc.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.covalent.CovalentGateway;
import network.arkane.provider.balance.EvmCovalentBalanceStrategy;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "bsc.balance.strategy", havingValue = "covalent")
public class BscCovalentBalanceStrategy extends EvmCovalentBalanceStrategy {


    public BscCovalentBalanceStrategy(BscWeb3JGateway web3JGateway,
                                      TokenDiscoveryService tokenDiscoveryService,
                                      CovalentGateway covalentGateway,
                                      @Value("${covalent.chain-id.BSC}") String chainId) {
        super(web3JGateway, tokenDiscoveryService, covalentGateway, chainId);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
