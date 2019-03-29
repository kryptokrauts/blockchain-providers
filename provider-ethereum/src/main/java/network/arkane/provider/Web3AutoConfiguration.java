package network.arkane.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

@Configuration
public class Web3AutoConfiguration {
    @Bean(name = "ethereumWeb3j")
    @Primary
    public Web3j ethereumWeb3j(final @Value("${network.arkane.ethereum.endpoint.url}") String ethereumEndpoint) {
        if (ethereumEndpoint.startsWith("ws")) {
            return Web3j.build(new WebSocketService(ethereumEndpoint, false));
        } else {
            return Web3j.build(new HttpService(ethereumEndpoint, false));
        }
    }
}
