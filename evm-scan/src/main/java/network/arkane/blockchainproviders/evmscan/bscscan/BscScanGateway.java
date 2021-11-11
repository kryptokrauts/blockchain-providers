package network.arkane.blockchainproviders.evmscan.bscscan;

import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class BscScanGateway extends EvmScanGateway {

    public BscScanGateway(final BscScanClient bscScanClient) {
        super(bscScanClient);
    }

    @Override
    public SecretType secretType() {
        return SecretType.BSC;
    }
}
