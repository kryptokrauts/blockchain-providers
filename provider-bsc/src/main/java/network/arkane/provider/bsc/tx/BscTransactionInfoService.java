package network.arkane.provider.bsc.tx;

import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.EvmTransactionInfoService;
import network.arkane.provider.tx.HasReachedFinalityService;
import org.springframework.stereotype.Component;

@Component
public class BscTransactionInfoService extends EvmTransactionInfoService {

    public BscTransactionInfoService(final BscWeb3JGateway bscWeb3JGateway,
                                     final HasReachedFinalityService hasReachedFinalityService) {
        super(bscWeb3JGateway.web3(), hasReachedFinalityService);
    }

    public SecretType type() {
        return SecretType.BSC;
    }

}
