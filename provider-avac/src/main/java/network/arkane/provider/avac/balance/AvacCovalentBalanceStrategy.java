package network.arkane.provider.avac.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.covalent.CovalentGateway;
import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.balance.EvmCovalentBalanceStrategy;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "avac.balance.strategy", havingValue = "covalent")
public class AvacCovalentBalanceStrategy extends EvmCovalentBalanceStrategy {


    public AvacCovalentBalanceStrategy(AvacWeb3JGateway web3JGateway,
                                       TokenDiscoveryService tokenDiscoveryService,
                                       CovalentGateway covalentGateway,
                                       @Value("${covalent.chain-id.AVAC}") String chainId) {
        super(web3JGateway, tokenDiscoveryService, covalentGateway, chainId);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
