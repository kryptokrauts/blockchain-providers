package network.arkane.blockchainproviders.evmscan.polygonscan;

import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

@Component
public class PolygonscanGateway extends EvmScanGateway {

    public PolygonscanGateway(final PolygonscanClient polygonscanClient) {
        super(polygonscanClient);
    }

    @Override
    public SecretType secretType() {
        return SecretType.MATIC;
    }
}
