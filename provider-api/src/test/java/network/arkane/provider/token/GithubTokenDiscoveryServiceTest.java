package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GithubTokenDiscoveryServiceTest {
    private GithubTokenDiscoveryService tokenDiscoveryService;
    private TokenDiscoveryProperties properties;

    @BeforeEach
    public void setUp() {
        properties = new TokenDiscoveryProperties();
        properties.setOwner("ArkaneNetwork");
        properties.setRepo("content-management");
        HashMap<SecretType, String> paths = new HashMap<>();
        paths.put(SecretType.ETHEREUM, "tokens/ethereum/mainnet");
        properties.setPaths(paths);
        tokenDiscoveryService = new GithubTokenDiscoveryService(properties);
    }

    @Test
    public void getTokens() {
        Map<SecretType, Map<String, TokenInfo>> tokens = tokenDiscoveryService.getTokens();
        assertThat(tokens.get(SecretType.ETHEREUM)).isNotEmpty();

        TokenInfo tokenInfo = tokens.get(SecretType.ETHEREUM).get("0x4df47b4969b2911c966506e3592c41389493953b");
        assertThat(tokenInfo.getLogo()).isEqualTo("https://raw.githubusercontent"
                                                  + ".com/ArkaneNetwork/content-management/master/tokens/ethereum/mainnet/logos/" + tokenInfo.getAddress() + ".png");
    }
}