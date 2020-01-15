package network.arkane.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
