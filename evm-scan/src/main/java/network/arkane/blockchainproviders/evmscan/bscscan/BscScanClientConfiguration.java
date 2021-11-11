package network.arkane.blockchainproviders.evmscan.bscscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClientConfiguration;
import org.springframework.beans.factory.annotation.Value;

public class BscScanClientConfiguration extends EvmScanClientConfiguration {

    public BscScanClientConfiguration(@Value("${bscscan.api-tokens:}") final String apiTokens) {
        super(apiTokens);
    }
}
