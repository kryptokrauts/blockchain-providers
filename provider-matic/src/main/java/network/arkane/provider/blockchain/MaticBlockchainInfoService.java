package network.arkane.provider.blockchain;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class MaticBlockchainInfoService extends EvmBlockchainInfoService {
    public MaticBlockchainInfoService(MaticWeb3JGateway maticWeb3jGateway) {
        super(maticWeb3jGateway.web3());
    }

    public SecretType type() {
        return SecretType.MATIC;
    }
}
