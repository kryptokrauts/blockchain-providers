package network.arkane.provider.avac.tx;

import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.EvmTransactionInfoService;
import network.arkane.provider.tx.HasReachedFinalityService;
import org.springframework.stereotype.Component;

@Component
public class AvacTransactionInfoService extends EvmTransactionInfoService {

    public AvacTransactionInfoService(final AvacWeb3JGateway avacWeb3JGateway,
                                      final HasReachedFinalityService hasReachedFinalityService) {
        super(avacWeb3JGateway.web3(), hasReachedFinalityService);
    }

    public SecretType type() {
        return SecretType.AVAC;
    }

}
