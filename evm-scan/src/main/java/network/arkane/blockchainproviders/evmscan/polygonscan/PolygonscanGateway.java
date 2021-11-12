package network.arkane.blockchainproviders.evmscan.polygonscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PolygonscanGateway extends EvmScanGateway {

    public PolygonscanGateway(@Value("${polygonscan.api-base-url}") final String apiBaseUrl,
                              @Value("${polygonscan.api-tokens:}") final String apiTokens) {
        super(new EvmScanClient(apiBaseUrl, apiTokens));
    }

    @Override
    public SecretType secretType() {
        return SecretType.MATIC;
    }
}
