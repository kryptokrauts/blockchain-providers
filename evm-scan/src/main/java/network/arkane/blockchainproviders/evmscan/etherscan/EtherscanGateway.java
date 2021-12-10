package network.arkane.blockchainproviders.evmscan.etherscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EtherscanGateway extends EvmScanGateway {


    public EtherscanGateway(@Value("${etherscan.api-base-url}") final String apiBaseUrl,
                            @Value("${etherscan.api-tokens:}") final String apiTokens) {
        super(new EvmScanClient(apiBaseUrl, apiTokens));
    }

    @Override
    public SecretType secretType() {
        return SecretType.ETHEREUM;
    }
}
