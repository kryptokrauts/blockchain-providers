package network.arkane.provider.avac.blockchain;

import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.blockchain.EvmBlockchainInfoService;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class AvacBlockchainInfoService extends EvmBlockchainInfoService {
    public AvacBlockchainInfoService(AvacWeb3JGateway avacWeb3JGateway) {
        super(avacWeb3JGateway.web3());
    }

    public SecretType type() {
        return SecretType.AVAC;
    }
}
