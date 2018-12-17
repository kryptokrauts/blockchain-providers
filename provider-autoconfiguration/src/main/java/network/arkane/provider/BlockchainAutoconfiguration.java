package network.arkane.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackageClasses = BlockchainAutoconfiguration.class)
@Slf4j
public class BlockchainAutoconfiguration {

    @PostConstruct
    public void init() {
        log.info("Starting autoconfiguration for blockchain providers");
    }
}
