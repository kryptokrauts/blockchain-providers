package network.arkane.provider.nonfungible.metadata;

import network.arkane.provider.chain.SecretType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "network.arkane.nonfungible.discovery.github"
)
public class NonFungibleDiscoveryProperties {
    private Map<SecretType, String> paths;
    private String owner;
    private String repo;

    public Map<SecretType, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<SecretType, String> paths) {
        this.paths = paths;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
}
