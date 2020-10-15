package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.GochainWeb3JGateway;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GochainTransactionGateway extends EvmTransactionGateway {

    public GochainTransactionGateway(GochainWeb3JGateway web3j) {
        super(web3j.web3());
    }

    @Override
    public SecretType getType() {
        return SecretType.GOCHAIN;
    }

}
