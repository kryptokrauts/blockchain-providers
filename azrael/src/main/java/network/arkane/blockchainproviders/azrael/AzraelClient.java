package network.arkane.blockchainproviders.azrael;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.TokenBalance;
import network.arkane.blockchainproviders.azrael.dto.contract.ContractDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class AzraelClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AzraelClient(String baseUrl) {
        this.baseUrl = baseUrl;
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory();
        restTemplate = new RestTemplateBuilder().uriTemplateHandler(defaultUriTemplateHandler)
                                                .defaultMessageConverters()
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .setReadTimeout(Duration.ofSeconds(30))
                                                .build();
    }

    public Optional<ContractDto> getContract(String address) {
        return Optional.ofNullable(restTemplate.getForObject(baseUrl + "/contracts/{address}", ContractDto.class, address));
    }

    public List<TokenBalance> getTokens(String address) {
        return getTokens(address, emptyList());
    }

    public List<TokenBalance> getTokens(String address,
                                        List<ContractType> types) {

        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("address", address);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/{address}/tokens");
        types.forEach(t -> builder.queryParam("type", t));

        ResponseEntity<TokenBalance[]> result = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(new LinkedMultiValueMap()),
                                                                      TokenBalance[].class, uriParam);

        return result.getBody() == null
               ? emptyList()
               : Arrays.asList(result.getBody());
    }
}
