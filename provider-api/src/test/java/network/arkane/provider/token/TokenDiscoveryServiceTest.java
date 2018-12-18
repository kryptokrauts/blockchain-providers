package network.arkane.provider.token;

import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenDiscoveryServiceTest {
    private GithubTokenDiscoveryService githubTokenDiscoveryService;
    private TokenDiscoveryService tokenDiscoveryService;
    private BalanceGateway ethereumBridge;
    private BalanceGateway vechainBridge;

    @BeforeEach
    void setUp() {
        githubTokenDiscoveryService = mock(GithubTokenDiscoveryService.class);
        ethereumBridge = mock(BalanceGateway.class);
        vechainBridge = mock(BalanceGateway.class);
        when(ethereumBridge.type()).thenReturn(SecretType.ETHEREUM);
        when(vechainBridge.type()).thenReturn(SecretType.VECHAIN);
        tokenDiscoveryService = new TokenDiscoveryService(githubTokenDiscoveryService, Arrays.asList(ethereumBridge, vechainBridge));
    }

    @Test
    void getTokens() {
        Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        tokens.get(SecretType.ETHEREUM).put("0x0", new TokenInfo());
        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);

        assertThat(tokenDiscoveryService.getTokens(SecretType.ETHEREUM).get(0)).isNotNull();
    }

    @Test
    void getTokenInfoEmptyForChain() {
        final String tokenAddress = "0x0";
        when(githubTokenDiscoveryService.getTokens()).thenReturn(new HashMap<>());
        when(ethereumBridge.getTokenInfo(tokenAddress)).thenReturn(Optional.empty());

        assertThat(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress)).isEmpty();
    }


    @Test
    void getTokenInfoEmptyForToken() {
        final String tokenAddress = "0x0";
        Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);
        when(ethereumBridge.getTokenInfo(tokenAddress)).thenReturn(Optional.empty());

        assertThat(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress)).isEmpty();
    }

    @Test
    public void getTokenInfo_noGitHubInfoForToken_ETHEREUM() {
        final String tokenAddress = "0x0";
        final Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        final TokenInfo expected = new TokenInfo();

        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);
        when(ethereumBridge.getTokenInfo(tokenAddress)).thenReturn(Optional.of(expected));

        final Optional<TokenInfo> result = tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress);

        assertThat(result).isNotEmpty().containsSame(expected);
    }

    @Test
    public void getTokenInfo_noGitHubInfoForToken_VECHAIN() {
        final String tokenAddress = "0x0";
        final Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.VECHAIN, new HashMap<>());
        final TokenInfo expected = new TokenInfo();

        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);
        when(vechainBridge.getTokenInfo(tokenAddress)).thenReturn(Optional.of(expected));

        final Optional<TokenInfo> result = tokenDiscoveryService.getTokenInfo(SecretType.VECHAIN, tokenAddress);

        assertThat(result).isNotEmpty().containsSame(expected);
    }

    @Test
    void getTokenInfoForToken() {
        Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        tokens.get(SecretType.ETHEREUM).put("0x0", new TokenInfo());
        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);
        assertThat(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, "0x0")).isNotNull();
    }
}