package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.client.MaticBlockscoutClient;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.GithubTokenDiscoveryService;
import network.arkane.provider.token.NativeTokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaticBlockscoutDiscoveryService {

    private GithubTokenDiscoveryService githubTokenDiscoveryService;
    private final Map<SecretType, ? extends NativeTokenDiscoveryService> tokenDiscoveryServices;
    private MaticBlockscoutClient maticBlockscoutClient;

    public MaticBlockscoutDiscoveryService(final GithubTokenDiscoveryService githubTokenDiscoveryService,
                                           final List<? extends NativeTokenDiscoveryService> tokenDiscoveryServices,
                                           final MaticBlockscoutClient maticBlockscoutClient) {
        this.githubTokenDiscoveryService = githubTokenDiscoveryService;
        this.tokenDiscoveryServices = tokenDiscoveryServices.stream().collect(Collectors.toMap(NativeTokenDiscoveryService::type, Function.identity()));
        this.maticBlockscoutClient = maticBlockscoutClient;
    }

    @Cacheable(value = "tokenInfo")
    public Optional<TokenInfo> getTokenInfo(String tokenAddress) {
        final Optional<TokenInfo> tokenInfo = Optional.ofNullable(githubTokenDiscoveryService.getTokens().get(SecretType.MATIC)).map(t -> t.get(tokenAddress));
        return tokenInfo.isPresent() ? tokenInfo : fetchFromChain(tokenAddress);
    }

    private Optional<TokenInfo> fetchFromChain(String tokenAddress) {
        try {
            return tokenDiscoveryServices.get(SecretType.MATIC).getTokenInfo(tokenAddress);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<MaticBlockscoutTokenResponse> getTokens(String walletAddress) {
        try {
            MaticBlockscoutResponse<List<MaticBlockscoutTokenResponse>> tokensForAddress = maticBlockscoutClient.getTokensForAddress(walletAddress);
            if (tokensForAddress.getStatus().equals("1")) {
                return tokensForAddress.getResult();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception ex) {
            log.error("Something went wrong while trying to fetch matic tokens", ex);
            return Collections.emptyList();
        }
    }
}
