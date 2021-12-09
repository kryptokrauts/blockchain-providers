package network.arkane.blockchainproviders.covalent;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.covalent.dto.CovalentApiResponse;
import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import network.arkane.blockchainproviders.covalent.dto.CovalentTxHistoryResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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

    @Retryable(value = Exception.class,
               backoff = @Backoff(delay = 1000L, multiplier = 2))
    public CovalentTokenBalanceResponse getTokenBalances(String chainId,
                                                         String address) {
        return restTemplate.getForObject("/{chainId}/address/{address}/balances_v2/", CovalentTokenBalanceResponse.class, chainId, address);
    }

    public CovalentApiResponse<CovalentTxHistoryResponse> getTxHistory(final Long chainId,
                                                                       final String walletAddress) {
        return restTemplate.exchange("/{chain_id}/address/{address}/transactions_v2/",
                                     HttpMethod.GET,
                                     null,
                                     new ParameterizedTypeReference<CovalentApiResponse<CovalentTxHistoryResponse>>() {},
                                     chainId, walletAddress)
                           .getBody();
    }
}
