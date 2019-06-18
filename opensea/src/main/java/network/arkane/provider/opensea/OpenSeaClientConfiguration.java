package network.arkane.provider.opensea;

import feign.Logger;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

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
    public RequestInterceptor apiTokenInterceptor() {
        return requestTemplate -> {
            if (StringUtils.isNotBlank(apiToken) && !requestTemplate.headers().containsKey(API_KEY_KEY)) {
                requestTemplate.header(API_KEY_KEY, apiToken);
            }
        };
    }
}