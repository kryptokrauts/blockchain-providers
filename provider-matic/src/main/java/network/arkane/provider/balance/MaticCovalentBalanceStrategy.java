package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.covalent.CovalentClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "matic.balance.strategy", havingValue = "covalent")
public class MaticCovalentBalanceStrategy extends EvmCovalentBalanceStrategy {


    public MaticCovalentBalanceStrategy(MaticWeb3JGateway web3JGateway,
                                        @Value("${covalent.endpoint}") String endpoint,
                                        @Value("${covalent.api-key}") String apiKey,
                                        @Value("${covalent.chain-id.MATIC}") String chainId) {
        super(web3JGateway, new CovalentClient(endpoint, apiKey), chainId);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

}
