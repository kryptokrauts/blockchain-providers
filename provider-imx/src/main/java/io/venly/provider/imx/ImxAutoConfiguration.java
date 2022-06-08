package io.venly.provider.imx;

import io.venly.provider.imx.balance.ImxBalanceGateway;
import io.venly.provider.imx.config.ImxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@EnableConfigurationProperties({ImxProperties.class})
public class ImxAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("provider-imx loaded");
    }

    @Bean
    @ConditionalOnProperty(prefix = "io.venly.provider.imx-gateway", name = {"endpoint", "user", "password"})
    @ConditionalOnMissingBean(ImxBalanceGateway.class)
    ImxBalanceGateway imxBalanceGateway(ImxProperties imxProperties) {
        return new ImxBalanceGateway(imxProperties);
    }

}
