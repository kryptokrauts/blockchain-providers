package network.arkane.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.opensea.OpenSeaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {OpenSeaClient.class})
@ImportAutoConfiguration( {FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
public class EthereumAutoConfiguration {

    @Bean(name = "ethereumBlockscoutClient")
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotBlank('${blockscout.ethereum.url:}')")
    public BlockscoutClient ethereumBlockscoutClient(@Value("${blockscout.ethereum.url}") String baseUrl,
                                                     ObjectMapper objectMapper) {
        return new BlockscoutClient(baseUrl, objectMapper);
    }

}
