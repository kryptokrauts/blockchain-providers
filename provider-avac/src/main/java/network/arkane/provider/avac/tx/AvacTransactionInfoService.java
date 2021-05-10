package network.arkane.provider.avac.tx;

import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.EvmTransactionInfoService;
import org.springframework.stereotype.Component;

@Component
public class AvacTransactionInfoService extends EvmTransactionInfoService {

    public AvacTransactionInfoService(AvacWeb3JGateway avacWeb3JGateway) {
        super(avacWeb3JGateway.web3());
    }

    public SecretType type() {
        return SecretType.AVAC;
    }

}
