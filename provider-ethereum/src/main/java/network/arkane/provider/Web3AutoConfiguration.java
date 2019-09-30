package network.arkane.provider;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.gateway.EthereumWeb3JGatewayFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@Slf4j
public class Web3AutoConfiguration {

    private WebSocketClient websocket;

    @Scheduled(fixedDelay = 10_000)
    public void assureConnection() {
        if (websocket != null && !websocket.isOpen()) {
            try {
                websocket.reconnect();
            } catch (final Exception ex) {
                log.error("Unable to reconnect to arkane ws {}", ex.getMessage());
            }
        }
    }

    @Primary
    @Bean(name = "ethereumWeb3j")
    public Web3j ethereumWeb3j(final @Value("${network.arkane.ethereum.endpoint.url}") String endpoint) {
        if (endpoint.startsWith("ws")) {
            this.websocket = new WebSocketClient(URI.create(endpoint));
            return Web3j.build(new WebSocketService(this.websocket, false));
        } else {
            return Web3j.build(new HttpService(endpoint, createOkHttpClient(), false));
        }
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configureLogging(builder);
        configureTimeouts(builder);
        return builder.build();
    }

    private void configureTimeouts(OkHttpClient.Builder builder) {
        long tos = 60L;
        builder.connectTimeout(tos, TimeUnit.SECONDS);
        builder.readTimeout(tos, TimeUnit.SECONDS);
        builder.writeTimeout(tos, TimeUnit.SECONDS);
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::error);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    @Bean
    public EthereumWeb3JGateway ethereumWeb3JGateway(final EthereumWeb3JGatewayFactory ethereumWeb3JGatewayFactory) {
        return ethereumWeb3JGatewayFactory.getInstance();
    }
}
