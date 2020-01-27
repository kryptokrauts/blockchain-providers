package network.arkane.provider.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class MaticWeb3JGatewayFactory {

    private final MaticWeb3JGateway defaultWeb3jGateway;
    private final String deltaBalancesAddress;

    public MaticWeb3JGatewayFactory(final @Qualifier("maticWeb3j") Web3j maticWeb3j,
                                    final @Value("${network.arkane.matic.deltabalances.contract-address}") String deltaBalancesAddress) {
        this.deltaBalancesAddress = deltaBalancesAddress;
        this.defaultWeb3jGateway = new MaticWeb3JGateway(maticWeb3j, deltaBalancesAddress);
    }

    public MaticWeb3JGateway getInstance() {
        return defaultWeb3jGateway;
    }

    public MaticWeb3JGateway getInstance(final String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return defaultWeb3jGateway;
        } else {
            return new MaticWeb3JGateway(Web3j.build(new HttpService(endpoint, false)), this.deltaBalancesAddress);
        }
    }
}
