package network.arkane.blockchainproviders.evmscan.etherscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClientConfiguration;
import org.springframework.beans.factory.annotation.Value;

public class EtherscanClientConfiguration extends EvmScanClientConfiguration {

    public EtherscanClientConfiguration(@Value("${etherscan.api-tokens:}") final String apiTokens) {
        super(apiTokens);
    }
}
