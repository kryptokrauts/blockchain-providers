package network.arkane.provider.gateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
public class EthereumWeb3JGatewayFactory {

    private final EthereumWeb3JGateway defaultEthereumGateway;
    private final String deltaBalancesAddress;

    public EthereumWeb3JGatewayFactory(final @Qualifier("ethereumWeb3j") Web3j ethereumWeb3j,
                                       final @Value("${network.arkane.ethereum.deltabalances.contract-address}") String deltaBalancesAddress) {
        this.deltaBalancesAddress = deltaBalancesAddress;
        this.defaultEthereumGateway = new EthereumWeb3JGateway(ethereumWeb3j, deltaBalancesAddress);
    }

    public EthereumWeb3JGateway getInstance() {
        return defaultEthereumGateway;
    }

    public EthereumWeb3JGateway getInstance(final String endpoint) {
        if (StringUtils.isBlank(endpoint)) {
            return defaultEthereumGateway;
        } else {
            return new EthereumWeb3JGateway(Web3j.build(new HttpService(endpoint, false)), this.deltaBalancesAddress);
        }
    }
}
