package network.arkane.blockchainproviders.evmscan.polygonscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClientConfiguration;
import org.springframework.beans.factory.annotation.Value;

public class PolygonscanClientConfiguration extends EvmScanClientConfiguration {

    public PolygonscanClientConfiguration(@Value("${polygonscan.api-tokens:}") final String apiTokens) {
        super(apiTokens);
    }
}
