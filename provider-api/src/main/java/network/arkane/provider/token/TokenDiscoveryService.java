package network.arkane.provider.token;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TokenDiscoveryService {

    private final GithubTokenDiscoveryService githubTokenDiscoveryService;
    private final Map<SecretType, TransactionGateway> bridges;

    public TokenDiscoveryService(final GithubTokenDiscoveryService githubTokenDiscoveryService,
                                 final Map<SecretType, TransactionGateway> bridges) {
        this.githubTokenDiscoveryService = githubTokenDiscoveryService;
        this.bridges = bridges;
    }

    public List<TokenInfo> getTokens(final SecretType chain) {
        Map<String, TokenInfo> tokens = githubTokenDiscoveryService.getTokens().get(chain);
        return new ArrayList<>(tokens.values());
    }

    @Cacheable(value = "tokenInfo")
    public Optional<TokenInfo> getTokenInfo(final SecretType chain, final String tokenAddress) {
        final Optional<TokenInfo> tokenInfo = Optional.ofNullable(githubTokenDiscoveryService.getTokens().get(chain)).map(t -> t.get(tokenAddress));
        return tokenInfo.isPresent() ? tokenInfo : fetchFromChain(chain, tokenAddress);
    }

    private Optional<TokenInfo> fetchFromChain(SecretType chain, String tokenAddress) {
        try {
            return bridges.get(chain).getTokenInfo(tokenAddress);
        } catch (Exception e) {
            return Optional.empty();
        }

    }
}
