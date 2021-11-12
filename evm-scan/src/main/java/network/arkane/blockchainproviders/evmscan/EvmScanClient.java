package network.arkane.blockchainproviders.evmscan;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import network.arkane.blockchainproviders.evmscan.dto.EvmScanApiResponse;
import network.arkane.blockchainproviders.evmscan.dto.EvmTransaction;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;

import static org.springframework.util.StringUtils.isEmpty;

public class EvmScanClient {

    private final RestTemplate restTemplate;
    private final Iterator<String> apiKeys;


    public EvmScanClient(final String apiBaseUrl,
                         final String apiTokens) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory(apiBaseUrl);
        restTemplate = new RestTemplateBuilder().defaultMessageConverters()
                                                .uriTemplateHandler(defaultUriTemplateHandler)
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .additionalInterceptors(new RestTemplateUserAgentInterceptor())
                                                .setReadTimeout(Duration.ofSeconds(60))
                                                .build();
        this.apiKeys = !isEmpty(apiTokens) ? Iterables.cycle(apiTokens.split(",")).iterator() : Collections.emptyIterator();
    }

    EvmScanApiResponse<EvmTransaction> getTransactionList(@RequestParam String address,
                                                          @RequestParam Long page,
                                                          @RequestParam Long offset) {
        var response = restTemplate.exchange("?module=account&action=txlist&sort=desc&address={address}&page={page}&offset={offset}&apikey={apiKey}",
                                             HttpMethod.GET,
                                             null,
                                             new ParameterizedTypeReference<EvmScanApiResponse<EvmTransaction>>() {},
                                             address,
                                             page,
                                             offset,
                                             apiKeys.next());
        return response.getBody();
    }

}
