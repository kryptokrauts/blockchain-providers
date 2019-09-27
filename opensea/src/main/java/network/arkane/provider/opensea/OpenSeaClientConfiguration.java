package network.arkane.provider.opensea;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OpenSeaClientConfiguration {

    private static final String API_KEY_KEY = "X-API-KEY";

    private final String apiToken;

    public OpenSeaClientConfiguration(@Value("${opensea.api-token:}") final String apiToken) {
        this.apiToken = apiToken;
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(2000, 3000);
    }

    @Bean
    public RequestInterceptor apiTokenInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("User-Agent", "curl/7.54.0");
            if (StringUtils.isNotBlank(apiToken) && !requestTemplate.headers().containsKey(API_KEY_KEY)) {
                requestTemplate.header(API_KEY_KEY, apiToken);
            }
        };
    }

    @Bean
    public RequestInterceptor removeEmptyQueryInterceptor() {
        return requestTemplate -> {
            final Map<String, Collection<String>> queries = requestTemplate.queries();
            requestTemplate.queries(null);
            requestTemplate.queries(queries.keySet()
                                           .stream()
                                           .filter(key -> {
                                               final Collection<String> value = queries.get(key);
                                               return value != null && !value.isEmpty();
                                           })
                                           .collect(Collectors.toMap(Function.identity(), queries::get)));

        };
    }
}