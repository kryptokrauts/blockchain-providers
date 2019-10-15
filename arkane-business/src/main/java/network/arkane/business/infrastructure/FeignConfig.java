package network.arkane.business.infrastructure;

import network.arkane.business.ArkaneBusiness;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {ArkaneBusiness.class})
public class FeignConfig {
}