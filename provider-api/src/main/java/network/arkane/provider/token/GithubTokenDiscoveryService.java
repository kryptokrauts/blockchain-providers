package network.arkane.provider.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.chain.SecretType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GithubTokenDiscoveryService {

    private final ObjectMapper objectMapper;
    private TokenDiscoveryProperties properties;
    private String githubRawPrefix = "https://raw.githubusercontent.com/ArkaneNetwork/content-management/master/";

    public GithubTokenDiscoveryService(TokenDiscoveryProperties properties) {
        this.properties = properties;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Cacheable(value = "tokens", key = "'all_tokens'")
    public Map<SecretType, Map<String, TokenInfo>> getTokens() {
        return Arrays.stream(SecretType.values())
                     .map(st -> Pair.of(st, properties.getPaths().get(st)))
                     .filter(p -> StringUtils.isNotBlank(p.getValue()))
                     .map(p -> Pair.of(p.getKey(), getTokens(githubRawPrefix + p.getValue() + "/details/all.json", p.getValue())))
                     .collect(Collectors.toMap(Pair::getKey, p -> p.getValue().stream().collect(Collectors.toMap(TokenInfo::getAddress, Function.identity()))));
    }

    private List<TokenInfo> getTokens(String allTokensUrl, String chainPath) {
        try {
            List<TokenInfo> tokenInfo = objectMapper.readValue(new URI(allTokensUrl).toURL(), new TypeReference<List<TokenInfo>>() {});
            for (TokenInfo info : tokenInfo) {
                info.setLogo(githubRawPrefix + chainPath + "/logos/" + info.getAddress() + ".png");
            }
            return tokenInfo;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
