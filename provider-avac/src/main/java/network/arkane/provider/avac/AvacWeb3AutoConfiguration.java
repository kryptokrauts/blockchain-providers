package network.arkane.provider.avac;

import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.provider.avac.gateway.AvacWeb3JGateway;
import network.arkane.provider.avac.gateway.AvacWeb3JGatewayFactory;
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
public class AvacWeb3AutoConfiguration {

    @Primary
    @Bean(name = "avacWeb3j")
    public Web3j avacWeb3j(final @Value("${network.arkane.avac.endpoint.url}") String endpoint) {
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

    @Bean
    public AvacWeb3JGateway avacWeb3JGateway(final AvacWeb3JGatewayFactory avacWeb3JGatewayFactory) {
        return avacWeb3JGatewayFactory.getInstance();
    }

    @Bean(name = "avacAzraelClient")
    public AzraelClient avacAzraelClient(@Value("${azrael.avac.url}") String baseUrl) {
        return new AzraelClient(baseUrl);
    }

}
