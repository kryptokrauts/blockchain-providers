package network.arkane.provider.avac.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;


@Component
@Slf4j
public class AvacWeb3JGateway extends EvmWeb3jGateway {

    @Override
    public SecretType getSecretType() {
        return SecretType.AVAC;
    }

    public AvacWeb3JGateway(@Qualifier("avacWeb3j") Web3j avacWeb3j,
                            final @Value("${network.arkane.avac.deltabalances.contract-address}") String deltaBalancesAddress) {
        super(avacWeb3j, deltaBalancesAddress);
    }

}
