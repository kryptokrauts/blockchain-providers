package network.arkane.provider.balance;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.BalanceMother;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BasicAuthCredentials;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static network.arkane.provider.BlockchainProviderTestHelper.jsonFromFile;
import static network.arkane.provider.BlockchainProviderTestHelper.randomString;
import static network.arkane.provider.BlockchainProviderTestHelper.verifyRequestSentWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;

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
    void retrievesBalance() {
        final String walletAddress = randomString();
        final String url = "/api/wallets/%s/balance".formatted(walletAddress);
        stubFor(get(url).willReturn(jsonFromFile("/balance/balance-response.json")));

        final Balance result = toTest.getBalance(walletAddress);

        verifyRequestSentWithAuthorization(url, user, password);

        assertThat(result)
                .isEqualTo(BalanceMother.imxBalance());

    }


}