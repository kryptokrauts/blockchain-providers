package network.arkane.provider.balance.client;

import network.arkane.provider.MaticWeb3AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {MaticWeb3AutoConfiguration.class})
public class MaticBlockscoutClientFeignConfig {
}
