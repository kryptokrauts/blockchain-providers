package network.arkane.provider.business.infrastructure;

import network.arkane.provider.business.ArkaneBusiness;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {ArkaneBusiness.class})
public class FeignConfig {
}