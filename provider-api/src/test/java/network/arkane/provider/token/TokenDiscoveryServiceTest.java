package network.arkane.provider.token;

import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenDiscoveryServiceTest {
    private GithubTokenDiscoveryService githubTokenDiscoveryService;
    private TokenDiscoveryService tokenDiscoveryService;
    private TransactionGateway ethereumBridge;
    private TransactionGateway vechainBridge;

    @BeforeEach
    public void setUp() throws Exception {
        githubTokenDiscoveryService = mock(GithubTokenDiscoveryService.class);
        ethereumBridge = mock(TransactionGateway.class);
        vechainBridge = mock(TransactionGateway.class);
        final Map<SecretType, TransactionGateway> bridges = new HashMap<>();
        bridges.put(SecretType.ETHEREUM, ethereumBridge);
        bridges.put(SecretType.VECHAIN, vechainBridge);
        tokenDiscoveryService = new TokenDiscoveryService(githubTokenDiscoveryService, bridges);
    }

    @Test
    public void getTokens() {
        Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        tokens.get(SecretType.ETHEREUM).put("0x0", new TokenInfo());
        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);

        assertThat(tokenDiscoveryService.getTokens(SecretType.ETHEREUM).get(0)).isNotNull();
    }

    @Test
    public void getTokenInfoEmptyForChain() {
        final String tokenAddress = "0x0";
        when(githubTokenDiscoveryService.getTokens()).thenReturn(new HashMap<>());
        when(ethereumBridge.getTokenInfo(tokenAddress)).thenReturn(Optional.empty());

        assertThat(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress)).isEmpty();
    }


    @Test
    public void getTokenInfoEmptyForToken() {
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
    public void getTokenInfoForToken() {
        Map<SecretType, Map<String, TokenInfo>> tokens = new HashMap<>();
        tokens.put(SecretType.ETHEREUM, new HashMap<>());
        tokens.get(SecretType.ETHEREUM).put("0x0", new TokenInfo());
        when(githubTokenDiscoveryService.getTokens()).thenReturn(tokens);

        assertThat(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, "0x0")).isNotNull();
    }

}