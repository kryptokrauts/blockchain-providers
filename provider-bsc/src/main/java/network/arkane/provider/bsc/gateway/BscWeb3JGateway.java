package network.arkane.provider.bsc.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;


@Component
@Slf4j
public class BscWeb3JGateway extends EvmWeb3jGateway {

    public BscWeb3JGateway(@Qualifier("bscWeb3j") Web3j bscWeb3j,
                           final @Value("${network.arkane.bsc.deltabalances.contract-address}") String deltaBalancesAddress) {
        super(bscWeb3j, deltaBalancesAddress);
    }

}
