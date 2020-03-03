package network.arkane.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.opensea.OpenSeaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {OpenSeaClient.class})
@ImportAutoConfiguration( {FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
public class MaticAutoConfiguration {

    @Bean(name = "maticBlockscoutClient")
    public BlockscoutClient maticBlockscoutClient(@Value("${blockscout.matic.url}") String baseUrl,
                                                  ObjectMapper objectMapper) {
        return new BlockscoutClient(baseUrl, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
