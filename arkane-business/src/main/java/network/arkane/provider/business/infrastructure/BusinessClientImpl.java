package network.arkane.provider.business.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Component
public class BusinessClientImpl implements BusinessClient {

    private RestTemplate restTemplate;

    public BusinessClientImpl(@Value("${feign.client.business.scheme}") String businessScheme,
                              @Value("${feign.client.business.host}") String businessHost) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory(businessScheme + "://" + businessHost);
        restTemplate = new RestTemplateBuilder().uriTemplateHandler(defaultUriTemplateHandler)
                                                .defaultMessageConverters()
                                                .setConnectTimeout(Duration.ofSeconds(2))
                                                .additionalInterceptors(new RestTemplateUserAgentInterceptor())
                                                .setReadTimeout(Duration.ofSeconds(30))
                                                .build();
    }

    @Override
    public List<TokenDto> getTokensForAddress(String userAddress) {
        TokenDto[] result = restTemplate.getForObject("/api/{userAddress}/items", TokenDto[].class, userAddress);
        return result == null ? Collections.emptyList() : Arrays.asList(result);
    }

    @Override
    public TokenContract getContract(String contractAddress) {
        return nullWhen404(() -> restTemplate.getForObject("/api/contracts/{contractAddress}", TokenContract.class, contractAddress));
    }

    @Override
    public TokenDto getToken(String contractAddress,
                             BigInteger tokenId) {
        return nullWhen404(() -> restTemplate.getForObject("/api/contracts/{contractAddress}/tokens/{tokenId}", TokenDto.class, contractAddress, tokenId));
    }

    public <T> T nullWhen404(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
