package network.arkane.provider.contract;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class EthereumContractService extends EvmContractService implements ContractService {

    public EthereumContractService(EthereumWeb3JGateway ethereumWeb3JGateway) {
        super(ethereumWeb3JGateway);
    }


}
