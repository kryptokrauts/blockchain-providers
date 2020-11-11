package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "network.arkane.nft.contract.description.github"
)
public class NftContractDescriptionProperties {
    private Map<SecretType, String> paths;

    public Map<SecretType, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<SecretType, String> paths) {
        this.paths = paths;
    }

}
