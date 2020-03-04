package network.arkane.blockchainproviders.blockscout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.SneakyThrows;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutToken;
import network.arkane.provider.token.TokenInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class BlockscoutClient {

    private RestTemplate restTemplate;
    private ObjectReader blockscoutTokenReader;

    public BlockscoutClient(String baseUrl) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        blockscoutTokenReader = objectMapper.readerFor(new TypeReference<List<BlockscoutToken>>() {});
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory(baseUrl);
        restTemplate = new RestTemplateBuilder().uriTemplateHandler(defaultUriTemplateHandler)
                                                .defaultMessageConverters()
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .setReadTimeout(Duration.ofSeconds(30))
                                                .build();
    }

    public BigInteger getBalance(String address) {
        JsonNode response = restTemplate.getForObject("?module=account&action=balance&address={address}", JsonNode.class, address);
        return new BigInteger(response.get("result").asText("0"));
    }

    public BigInteger getTokenBalance(final String walletAddress,
                                      final String tokenAddress) {
        JsonNode response = restTemplate.getForObject("?module=account&action=tokenbalance&contractaddress={contractAddressHash}&address={addressHash}",
                                                      JsonNode.class,
                                                      tokenAddress,
                                                      walletAddress);
        return new BigInteger(response.get("result").asText("0"));

    }

    @SneakyThrows
    public List<BlockscoutToken> getTokenBalances(final String walletAddress) {
        JsonNode response = restTemplate.getForObject("?module=account&action=tokenlist&address={addressHash}",
                                                      JsonNode.class,
                                                      walletAddress);

        return blockscoutTokenReader.readValue(response.get("result"));
    }

    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        JsonNode response = restTemplate.getForObject("?module=token&action=getToken&contractaddress={contractAddressHash}",
                                                      JsonNode.class,
                                                      tokenAddress);
        if (response != null && isValidResponse(response)) {

            String type = response.get("result").get("type").asText();
            int decimals = response.get("result").get("decimals").asInt();
            String name = response.get("result").get("name").asText();
            String symbol = response.get("result").get("symbol").asText();
            return Optional.of(TokenInfo.builder()
                                        .type(type)
                                        .decimals(decimals)
                                        .name(name)
                                        .symbol(symbol)
                                        .address(tokenAddress)
                                        .transferable(true)
                                        .build());
        }
        return Optional.empty();
    }

    private boolean isValidResponse(JsonNode response) {
        return response.has("status") && response.get("status").asInt() == 1;
    }
}
