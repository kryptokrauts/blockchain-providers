package network.arkane.provider.avac.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class AvacWeb3JGatewayFactory {

    private final AvacWeb3JGateway defaultWeb3jGateway;
    private final String deltaBalancesAddress;

    public AvacWeb3JGatewayFactory(final @Qualifier("avacWeb3j") Web3j avacWeb3j,
                                   final @Value("${network.arkane.avac.deltabalances.contract-address}") String deltaBalancesAddress) {
        this.deltaBalancesAddress = deltaBalancesAddress;
        this.defaultWeb3jGateway = new AvacWeb3JGateway(avacWeb3j, deltaBalancesAddress);
    }

    public AvacWeb3JGateway getInstance() {
        return defaultWeb3jGateway;
    }

    public AvacWeb3JGateway getInstance(final String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return defaultWeb3jGateway;
        } else {
            return new AvacWeb3JGateway(Web3j.build(new HttpService(endpoint, false)), this.deltaBalancesAddress);
        }
    }
}
