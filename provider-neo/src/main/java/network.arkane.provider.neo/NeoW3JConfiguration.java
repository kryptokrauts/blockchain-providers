package network.arkane.provider.neo;

import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
@Slf4j
public class NeoW3JConfiguration {

    @Bean(name = "neow3j")
    @Primary
    public Neow3j neoNeow3j(final @Value("${network.arkane.neo.endpoint.url}") String endpoint) {
        if (endpoint != null && !endpoint.isEmpty()) {
            return Neow3j.build(new HttpService(endpoint, false));
        }else {
            return  Neow3j.build(new HttpService(false));
        }
    }
}
