package io.venly.provider.imx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@Import(ImxRawVerifier.class)
@EnableConfigurationProperties(ImxProperties.class)
public class ImxAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("provider-imx loaded");
    }

    /**
     * Having {@link ConditionalOnProperty @ConditionalOnProperty} allows this autoconfiguration to
     * silently disable all beans if the imx-gateway properties are missing.
     */
    @Bean
    @ConditionalOnProperty(prefix = "io.venly.provider.imx-gateway", name = {"endpoint", "user", "password"})
    ImxGatewayClient imxGatewayClient(ImxProperties imxProperties) {
        return new ImxGatewayClient(imxProperties);
    }

    @Bean
    @ConditionalOnBean(ImxGatewayClient.class)
    ImxBalanceGateway imxBalanceGateway(ImxGatewayClient imxGatewayClient) {
        return new ImxBalanceGateway(imxGatewayClient);
    }

}
