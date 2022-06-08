package network.arkane.provider;

import io.venly.provider.imx.ImxAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@EnableBlockchainProviders
@Import(ImxAutoConfiguration.class)
@Slf4j
public class BlockProvidersIT {

    @PostConstruct
    public void init() {
        log.info("Starting up Blockchain Providers");
    }

}
