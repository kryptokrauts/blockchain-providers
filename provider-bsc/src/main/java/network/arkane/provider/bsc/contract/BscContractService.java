package network.arkane.provider.bsc.contract;

import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.ContractService;
import network.arkane.provider.contract.EvmContractService;
import org.springframework.stereotype.Component;

@Component
public class BscContractService extends EvmContractService implements ContractService {

    public BscContractService(BscWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
