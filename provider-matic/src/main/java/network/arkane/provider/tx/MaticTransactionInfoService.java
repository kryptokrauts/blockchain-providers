package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class MaticTransactionInfoService extends EvmTransactionInfoService {

    public MaticTransactionInfoService(MaticWeb3JGateway maticWeb3jGateway) {
        super(maticWeb3jGateway.web3());
    }

    public SecretType type() {
        return SecretType.MATIC;
    }

}
