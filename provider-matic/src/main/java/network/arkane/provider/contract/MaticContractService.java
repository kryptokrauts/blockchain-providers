package network.arkane.provider.contract;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class MaticContractService extends EvmContractService implements ContractService {

    public MaticContractService(MaticWeb3JGateway maticWeb3jGateway) {
        super(maticWeb3jGateway);
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
