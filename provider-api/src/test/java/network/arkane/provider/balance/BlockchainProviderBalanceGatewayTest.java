package network.arkane.provider.balance;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.BalanceMother;
import network.arkane.provider.chain.SecretType;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class BlockchainProviderBalanceGatewayTest {

    @Test
    void shouldCallBalanceEndpoint(WireMockRuntimeInfo runtime) {
        final String user = "user";
        final String password = "password";
        final BlockchainProviderBalanceGateway toTest = new BlockchainProviderBalanceGateway(SecretType.IMX,
                                                                                             runtime.getHttpBaseUrl(),
                                                                                             new BasicAuthCredentials(
                                                                                       user,
                                                                                       password
                                                                               ));

        final String address = "0x123456";
        final String url = "/api/wallets/%s/balance".formatted(address);
        stubFor(get(url)
                        .willReturn(okJson(readFile("/balance-response.json")))
               );

        final Balance result = toTest.getBalance(address);
        final String expectedHeader = "Basic " + base64Encode(String.format("%s:%s", user, password));

        verify(getRequestedFor(urlEqualTo(url))
                       .withHeader("Authorization", equalTo(expectedHeader))
              );
        assertThat(result)
                .isEqualTo(BalanceMother.imxBalance());
    }

    private String base64Encode(final String s) {
        return encodeBase64String(s.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    private String readFile(final String fileName) {
        URL resource = this.getClass().getResource(fileName);
        return Files.readString(Paths.get(resource.toURI()));
    }

}