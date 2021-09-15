package network.arkane.provider.bsc.blockchain;

import network.arkane.provider.blockchain.EvmBlockchainInfoService;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class BscBlockchainInfoService extends EvmBlockchainInfoService {

    public BscBlockchainInfoService(BscWeb3JGateway bscWeb3JGateway) {
        super(bscWeb3JGateway.web3());
    }

    @Override
    public SecretType type() {
        return SecretType.BSC;
    }
}
