package network.arkane.provider.avac.contract;

import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.ContractService;
import network.arkane.provider.contract.EvmContractService;
import org.springframework.stereotype.Component;

@Component
public class AvacContractService extends EvmContractService implements ContractService {

    public AvacContractService(AvacWeb3JGateway web3JGateway) {
        super(web3JGateway);
    }

    @Override
    public SecretType type() {
        return SecretType.AVAC;
    }
}
