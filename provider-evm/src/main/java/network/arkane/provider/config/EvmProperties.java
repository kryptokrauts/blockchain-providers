package network.arkane.provider.config;

import network.arkane.provider.chain.SecretType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "network.arkane.evm"
)
public class EvmProperties {
    private Map<SecretType, Long> chainIds;

    public Map<SecretType, Long> getChainIds() {
        return chainIds;
    }

    public void setPaths(Map<SecretType, Long> paths) {
        this.chainIds = chainIds;
    }

}
