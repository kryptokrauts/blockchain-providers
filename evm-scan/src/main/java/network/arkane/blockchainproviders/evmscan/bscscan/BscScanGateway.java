package network.arkane.blockchainproviders.evmscan.bscscan;

import network.arkane.blockchainproviders.evmscan.EvmScanClient;
import network.arkane.blockchainproviders.evmscan.EvmScanGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BscScanGateway extends EvmScanGateway {

    public BscScanGateway(@Value("${bscscan.api-base-url}") final String apiBaseUrl,
                          @Value("${bscscan.api-tokens:}") final String apiTokens) {
        super(new EvmScanClient(apiBaseUrl, apiTokens));
    }

    @Override
    public SecretType secretType() {
        return SecretType.BSC;
    }
}
