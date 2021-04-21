package network.arkane.provider.avac.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.bridge.EvmTransactionGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AvacTransactionGateway extends EvmTransactionGateway {

    public AvacTransactionGateway(AvacWeb3JGateway web3j) {
        super(web3j.web3());
    }

    @Override
    public SecretType getType() {
        return SecretType.AVAC;
    }

}
