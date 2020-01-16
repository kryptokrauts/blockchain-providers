package network.arkane.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableBlockchainProviders
@Slf4j
public class BlockProvidersIT {

    @PostConstruct
    public void init() {
        log.info("Starting up Blockchain Providers");
    }

}
