package network.arkane.provider.balance.client;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class MaticBlockscoutClientConfiguration {

    public MaticBlockscoutClientConfiguration() {
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor userAgentInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("User-Agent", "curl/7.54.0");
        };
    }


    @Bean
    public Request.Options options() {
        return new Request.Options(2000, 3000);
    }
}
