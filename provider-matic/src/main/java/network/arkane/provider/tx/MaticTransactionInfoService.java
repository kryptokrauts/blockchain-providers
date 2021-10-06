package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;

@Component
public class MaticTransactionInfoService extends EvmTransactionInfoService {

    public MaticTransactionInfoService(final MaticWeb3JGateway maticWeb3jGateway,
                                       final HasReachedFinalityService hasReachedFinalityService) {
        super(maticWeb3jGateway.web3(), hasReachedFinalityService);
    }

    public SecretType type() {
        return SecretType.MATIC;
    }

}
