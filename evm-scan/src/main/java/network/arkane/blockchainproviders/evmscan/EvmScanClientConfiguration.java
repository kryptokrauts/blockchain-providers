package network.arkane.blockchainproviders.evmscan;

import com.google.common.collect.Iterables;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.Iterator;

import static org.springframework.util.StringUtils.isEmpty;

public abstract class EvmScanClientConfiguration {
    private static final String API_KEY = "apikey";

    private Iterator<String> apiTokens;

    public EvmScanClientConfiguration(final String apiTokens) {
        if (!isEmpty(apiTokens)) {
            this.apiTokens = Iterables.cycle(apiTokens.split(",")).iterator();
        }
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(10000, 30000);
    }

    @Bean
    public RequestInterceptor apiTokenInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("User-Agent", "curl/7.54.0");
            if (apiTokens != null && !requestTemplate.queries().containsKey(API_KEY)) {
                requestTemplate.query(API_KEY, apiTokens.next());
            }
        };
    }
}
