package network.arkane.provider.bsc.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class BscWeb3JGatewayFactory {

    private final BscWeb3JGateway defaultWeb3jGateway;
    private final String deltaBalancesAddress;

    public BscWeb3JGatewayFactory(final @Qualifier("bscWeb3j") Web3j bscWeb3j,
                                  final @Value("${network.arkane.bsc.deltabalances.contract-address}") String deltaBalancesAddress) {
        this.deltaBalancesAddress = deltaBalancesAddress;
        this.defaultWeb3jGateway = new BscWeb3JGateway(bscWeb3j, deltaBalancesAddress);
    }

    public BscWeb3JGateway getInstance() {
        return defaultWeb3jGateway;
    }

    public BscWeb3JGateway getInstance(final String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return defaultWeb3jGateway;
        } else {
            return new BscWeb3JGateway(Web3j.build(new HttpService(endpoint, false)), this.deltaBalancesAddress);
        }
    }
}
