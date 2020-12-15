package network.arkane.provider.bsc;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.provider.bsc.gateway.BscWeb3JGateway;
import network.arkane.provider.bsc.gateway.BscWeb3JGatewayFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class BscWeb3AutoConfiguration {

    @Primary
    @Bean(name = "bscWeb3j")
    public Web3j bscWeb3j(final @Value("${network.arkane.bsc.endpoint.url}") String endpoint) {
        return Web3j.build(new HttpService(endpoint, createOkHttpClient(), false));
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
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    @Bean(name = "bscBlockscoutClient")
    public BlockscoutClient bscBlockscoutClient(@Value("${blockscout.bsc.url}") String baseUrl) {
        return new BlockscoutClient(baseUrl);
    }

    @Bean
    public BscWeb3JGateway bscWeb3JGateway(final BscWeb3JGatewayFactory bscWeb3JGatewayFactory) {
        return bscWeb3JGatewayFactory.getInstance();
    }

    @Bean(name = "bscAzraelClient")
    public AzraelClient bscAzraelClient(@Value("${azrael.bsc.url}") String baseUrl) {
        return new AzraelClient(baseUrl);
    }

}
