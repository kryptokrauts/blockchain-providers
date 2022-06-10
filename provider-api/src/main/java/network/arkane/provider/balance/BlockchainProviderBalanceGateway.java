package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;
import java.util.List;

import static java.time.Duration.ofSeconds;

public class BlockchainProviderBalanceGateway extends BalanceGateway {

    private final static String BALANCE_URL = "/api/wallets/{address}/balance";
    private final static Duration CONNECT_TIMEOUT = ofSeconds(5);
    private final static Duration WRITE_TIMEOUT = ofSeconds(5);
    private final static Duration READ_TIMEOUT = ofSeconds(5);

    private final SecretType secretType;
    private final RestTemplate restTemplate;

    public BlockchainProviderBalanceGateway(
            final SecretType secretType,
            final String baseUrl,
            final BasicAuthCredentials basicAuth
                                           ) {

        restTemplate = new RestTemplateBuilder().uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                                                .requestFactory(() -> new OkHttp3ClientHttpRequestFactory(
                                                        new OkHttpClient.Builder()
                                                                .connectionPool(new ConnectionPool())
                                                                .connectTimeout(CONNECT_TIMEOUT)
                                                                .writeTimeout(WRITE_TIMEOUT)
                                                                .readTimeout(READ_TIMEOUT)
                                                                .retryOnConnectionFailure(true)
                                                                .build()
                                                ))
                                                .additionalInterceptors(new BasicAuthenticationInterceptor(basicAuth.user(), basicAuth.password()))
                                                .build();
        this.secretType = secretType;
    }

    @Override
    public Balance getBalance(final String address) {
        return restTemplate.getForObject(BALANCE_URL, Balance.class, address);
    }

    @Override
    public Balance getZeroBalance() {
        return Balance.builder()
                      .available(true)
                      .rawBalance("0")
                      .rawGasBalance("0")
                      .secretType(secretType)
                      .gasBalance(0.0)
                      .balance(0.0)
                      .symbol(secretType.getSymbol())
                      .gasSymbol(secretType.getGasSymbol())
                      .decimals(18)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String address,
                                               final List<String> tokenAddresses) {
        return List.of();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String address) {
        return List.of();
    }

    @Override
    public SecretType type() {
        return secretType;
    }

}
