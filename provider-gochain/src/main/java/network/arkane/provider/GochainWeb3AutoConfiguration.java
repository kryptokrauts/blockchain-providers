package network.arkane.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class GochainWeb3AutoConfiguration {
    @Bean
    @Qualifier("gochainWeb3j")
    public Web3j gochainWeb3j(final @Value("${network.arkane.gochain.endpoint.url}") String gochainEndpoint) {
        return Web3j.build(new HttpService(gochainEndpoint));
    }
}
