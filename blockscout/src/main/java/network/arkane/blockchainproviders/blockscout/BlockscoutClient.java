package network.arkane.blockchainproviders.blockscout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.SneakyThrows;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutToken;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutTokenBalance;
import network.arkane.blockchainproviders.blockscout.dto.ERC1155BlockscoutToken;
import network.arkane.blockchainproviders.blockscout.dto.ERC20BlockscoutToken;
import network.arkane.blockchainproviders.blockscout.dto.ERC721BlockscoutToken;
import network.arkane.provider.token.TokenInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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

    public Map<String, BigInteger> getTokenBalances(final String walletAddress,
                                                    final List<String> tokenAddresses) {
        //        JsonNode response = restTemplate.getForObject("?module=account&action=tokenbalance&contractaddress={contractAddressHash}&address={addressHash}",
        //                                                      JsonNode.class,
        //                                                      tokenAddress,
        //                                                      walletAddress);
        //        String result = response.get("result").asText("0");
        //        return new BigInteger(StringUtils.isEmpty(result) ? "0" : result);

        final List<String> lowerCaseTokenAddresses = tokenAddresses.stream().map(String::toLowerCase).collect(Collectors.toList());
        return getTokenBalances(walletAddress).stream()
                                              .filter(token -> lowerCaseTokenAddresses.contains(token.getContractAddress().toLowerCase()))
                                              .collect(toMap(token -> token.getContractAddress().toLowerCase(), this::getTokenCount));
    }

    public BigInteger getTokenBalance(final String walletAddress,
                                      final String tokenAddress) {
        //        JsonNode response = restTemplate.getForObject("?module=account&action=tokenbalance&contractaddress={contractAddressHash}&address={addressHash}",
        //                                                      JsonNode.class,
        //                                                      tokenAddress,
        //                                                      walletAddress);
        //        String result = response.get("result").asText("0");
        //        return new BigInteger(StringUtils.isEmpty(result) ? "0" : result);

        return getTokenBalances(walletAddress)
                .stream()
                .filter(x -> x.getContractAddress().equalsIgnoreCase(tokenAddress))
                .map(this::getTokenCount)
                .findFirst().orElse(BigInteger.ZERO);

    }

    private BigInteger getTokenCount(BlockscoutToken token) {
        if (token.getType().equalsIgnoreCase("ERC-20")) {
            ERC20BlockscoutToken erc20Token = (ERC20BlockscoutToken) token;
            return erc20Token.getBalance();
        } else if (token.getType().equalsIgnoreCase("ERC-721")) {
            ERC721BlockscoutToken erc721Token = (ERC721BlockscoutToken) token;
            return erc721Token.getTokens() == null
                   ? BigInteger.ZERO
                   : erc721Token.getTokens().stream().map(BlockscoutTokenBalance::getBalance).reduce(BigInteger.ZERO, BigInteger::add);
        } else if (token.getType().equalsIgnoreCase("ERC-1155")) {
            ERC1155BlockscoutToken erc1155Token = (ERC1155BlockscoutToken) token;
            return erc1155Token.getTokens() == null
                   ? BigInteger.ZERO
                   : erc1155Token.getTokens().stream().map(BlockscoutTokenBalance::getBalance).reduce(BigInteger.ZERO, BigInteger::add);
        }
        return BigInteger.ZERO;
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
