package network.arkane.provider.bsc.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.EvmTransactionGateway;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BscTransactionGateway extends EvmTransactionGateway {

    public BscTransactionGateway(BscWeb3JGateway web3j) {
        super(web3j.web3());
    }

    @Override
    public SecretType getType() {
        return SecretType.BSC;
    }

}
