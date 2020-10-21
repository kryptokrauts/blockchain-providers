package network.arkane.provider.bsc.tx;

import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.EvmTransactionInfoService;
import org.springframework.stereotype.Component;

@Component
public class BscTransactionInfoService extends EvmTransactionInfoService {

    public BscTransactionInfoService(BscWeb3JGateway bscWeb3JGateway) {
        super(bscWeb3JGateway.web3());
    }

    public SecretType type() {
        return SecretType.BSC;
    }

}
