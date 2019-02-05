package network.arkane.provider.litecoin;

import network.arkane.provider.blockcypher.BlockcypherClient;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {BlockcypherClient.class})
@ImportAutoConfiguration(FeignAutoConfiguration.class)
public class LitecoinAutoConfiguration {
}
