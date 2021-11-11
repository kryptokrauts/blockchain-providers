package network.arkane.blockchainproviders.evmscan.etherscan;

import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class EtherscanGateway extends EvmScanGateway {

    public EtherscanGateway(final EtherscanClient etherscanClient) {
        super(etherscanClient);
    }

    @Override
    public SecretType secretType() {
        return SecretType.ETHEREUM;
    }
}
