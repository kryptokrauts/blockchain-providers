package network.arkane.provider.balance;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.BalanceMother;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.balance.domain.TokenBalanceMother;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BasicAuthCredentials;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.exceptions.BlockchainProviderResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static network.arkane.provider.BlockchainProviderTestHelper.jsonFromFile;
import static network.arkane.provider.BlockchainProviderTestHelper.randomString;
import static network.arkane.provider.BlockchainProviderTestHelper.verifyRequestSentWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest
class BlockchainProviderBalanceGatewayTest {

    final String user = randomString();
    final String password = randomString();

    private BlockchainProviderBalanceGateway toTest;

    @BeforeEach
    void setUp(WireMockRuntimeInfo runtime) {
        final BlockchainProviderGatewayClient gatewayClient = new BlockchainProviderGatewayClient(runtime.getHttpBaseUrl(),
                                                                                                  new BasicAuthCredentials(
                                                                                                          user,
                                                                                                          password
                                                                                                  ));
        toTest = new BlockchainProviderBalanceGateway(SecretType.IMX, gatewayClient);
    }

    @Test
    void retrieveBalance() {
        final String walletAddress = randomString();
        final String url = "/api/wallets/%s/balance".formatted(walletAddress);
        stubFor(get(url).willReturn(jsonFromFile("/balance/balance-response.json")));

        final Balance result = toTest.getBalance(walletAddress);

        verifyRequestSentWithAuthorization(url, user, password);

        assertThat(result)
                .isEqualTo(BalanceMother.imxBalance());
    }

    @Test
    void retrieveBalanceThrowNotFoundException() {
        final String walletAddress = randomString();
        final String url = "/api/wallets/%s/balance".formatted(walletAddress);
        stubFor(get(url).willReturn(jsonFromFile("/balance/balance-response-error.json", HttpStatus.NOT_FOUND)));

        assertThatThrownBy(() -> toTest.getBalance(walletAddress))
                .isInstanceOf(BlockchainProviderResourceNotFoundException.class);

    }

    @Test
    void retrieveTokenBalance() {
        final String walletAddress = randomString();
        final String url = "/api/wallets/%s/token-balances".formatted(walletAddress);
        stubFor(get(url).willReturn(jsonFromFile("/balance/token-balance-response.json")));

        final List<TokenBalance> result = toTest.getTokenBalances(walletAddress);

        verifyRequestSentWithAuthorization(url, user, password);

        assertThat(result)
                .containsExactly(TokenBalanceMother.imxResult());
    }

    @Test
    void retrieveTokenBalanceForTokenAddresses() {
        final String walletAddress = randomString();
        final String tokenAddress1 = randomString();
        final String tokenAddress2 = randomString();
        final String url = "/api/wallets/%s/token-balances?tokenAddresses[]=%s&tokenAddresses[]=%s".formatted(walletAddress, tokenAddress1, tokenAddress2);
        stubFor(get(url).willReturn(jsonFromFile("/balance/token-balance-response.json")));

        final List<TokenBalance> result = toTest.getTokenBalances(walletAddress, List.of(tokenAddress1, tokenAddress2));

        verifyRequestSentWithAuthorization(url, user, password);

        assertThat(result)
                .containsExactly(TokenBalanceMother.imxResult());
    }

    @Test
    void retrieveTokenBalanceThrowNotFoundException() {
        final String walletAddress = randomString();
        final String url = "/api/wallets/%s/token-balances".formatted(walletAddress);
        stubFor(get(url).willReturn(jsonFromFile("/balance/balance-response-error.json", HttpStatus.NOT_FOUND)));

        assertThatThrownBy(() -> toTest.getTokenBalances(walletAddress))
                .isInstanceOf(BlockchainProviderResourceNotFoundException.class);

    }
}