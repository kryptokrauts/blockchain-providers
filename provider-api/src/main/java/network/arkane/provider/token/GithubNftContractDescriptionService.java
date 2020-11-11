package network.arkane.provider.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
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
public class GithubNftContractDescriptionService {

    private final ObjectMapper objectMapper;
    private NftContractDescriptionProperties properties;
    private String githubRawPrefix = "https://raw.githubusercontent.com/ArkaneNetwork/content-management/master/";

    public GithubNftContractDescriptionService(NftContractDescriptionProperties properties) {
        this.properties = properties;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Cacheable(value = "nft-contract-descriptions", key = "'all_nft_contract_descriptions'")
    public Map<SecretType, Map<String, NonFungibleContract>> getContracts() {
        return Arrays.stream(SecretType.values())
                     .map(st -> Pair.of(st, properties.getPaths().get(st)))
                     .filter(p -> StringUtils.isNotBlank(p.getValue()))
                     .map(p -> Pair.of(p.getKey(), getTokens(githubRawPrefix + p.getValue() + "/all.json", p.getValue())))
                     .collect(Collectors.toMap(Pair::getKey, p -> p.getValue().stream().collect(Collectors.toMap(x -> x.getAddress().toLowerCase(), Function.identity()))));
    }

    private List<NonFungibleContract> getTokens(String url,
                                                String chainPath) {
        try {
            return objectMapper.readValue(new URI(url).toURL(), new TypeReference<List<NonFungibleContract>>() {});
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
