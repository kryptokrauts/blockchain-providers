package network.arkane.provider;

import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.opensea.OpenSeaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
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
    public BlockscoutClient maticBlockscoutClient(@Value("${blockscout.matic.url:}") String baseUrl) {
        return new BlockscoutClient(baseUrl);
    }

    @Bean(name = "maticAzraelClient")
    public AzraelClient maticAzraelClient(@Value("${azrael.matic.url:}") String baseUrl) {
        return new AzraelClient(baseUrl);
    }
}
