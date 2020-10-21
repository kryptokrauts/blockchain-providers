package network.arkane.provider.contract;

import network.arkane.provider.gateway.GochainWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class GochainContractService extends EvmContractService implements ContractService {

    private GochainWeb3JGateway maticWeb3jGateway;

    public GochainContractService(GochainWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

}
