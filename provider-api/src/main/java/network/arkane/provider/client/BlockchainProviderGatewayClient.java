package network.arkane.provider.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

import static java.time.Duration.ofSeconds;

public class BlockchainProviderGatewayClient {

    private final static Duration CONNECT_TIMEOUT = ofSeconds(5);
    private final static Duration WRITE_TIMEOUT = ofSeconds(5);
    private final static Duration READ_TIMEOUT = ofSeconds(5);

    private final RestTemplate restTemplate;

    public BlockchainProviderGatewayClient(final String baseUrl,
                                           final BasicAuthCredentials basicAuth) {
        restTemplate = new RestTemplateBuilder().uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                                                .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(
                                                        new OkHttpClient.Builder()
                                                                .connectionPool(new ConnectionPool())
                                                                .connectTimeout(CONNECT_TIMEOUT)
                                                                .writeTimeout(WRITE_TIMEOUT)
                                                                .readTimeout(READ_TIMEOUT)
                                                                .retryOnConnectionFailure(true)
                                                                .build()
                                                ))
                                                .additionalInterceptors(new BasicAuthenticationInterceptor(basicAuth.user(), basicAuth.password()))
                                                .errorHandler(new BlockchainProviderGatewayResponseErrorHandler(new ObjectMapper()))
                                                .build();
    }


    public <T> T get(final String url, final Class<T> responseType, final Object... parameters) {
        return restTemplate.getForObject(url, responseType, parameters);
    }

    public <T> T post(final String url, final Object request, final Class<T> responseType, final Object... parameters) {
        return restTemplate.postForObject(url, request, responseType, parameters);
    }
}
