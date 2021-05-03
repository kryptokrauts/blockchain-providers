package network.arkane.blockchainproviders.covalent;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

public class CovalentClient {

    private final RestTemplate restTemplate;

    public CovalentClient(String endpoint,
                          String apiKey) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory(endpoint);
        restTemplate = new RestTemplateBuilder().defaultMessageConverters()
                                                .uriTemplateHandler(defaultUriTemplateHandler)
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .additionalInterceptors(new RestTemplateUserAgentInterceptor())
                                                .additionalInterceptors(new BasicAuthenticationInterceptor(apiKey, ""))
                                                .setReadTimeout(Duration.ofSeconds(60))
                                                .build();
    }

    public CovalentTokenBalanceResponse getTokenBalances(String chainId,
                                                         String address) {
        return restTemplate.getForObject("/{chainId}/address/{address}/balances_v2/", CovalentTokenBalanceResponse.class, chainId, address);
    }
}
