package network.arkane.provider.nonfungible;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BasicAuthCredentials;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static network.arkane.provider.BlockchainProviderTestHelper.jsonFromFile;
import static network.arkane.provider.BlockchainProviderTestHelper.randomString;
import static network.arkane.provider.BlockchainProviderTestHelper.verifyRequestSentWithAuthorization;
import static network.arkane.provider.nonfungible.domain.NonFungibleAssetBalanceMother.aCryptoAssaultNonFungibleAssetBalance;
import static network.arkane.provider.nonfungible.domain.NonFungibleAssetBalanceMother.aGodsUnchainedNonFungibleAssetBalance;
import static network.arkane.provider.nonfungible.domain.NonFungibleAssetMother.aCryptoAssaultNonFungible;
import static network.arkane.provider.nonfungible.domain.NonFungibleContractMother.aCryptoAssaultNonFungibleContract;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class BlockchainProviderNonFungibleGatewayTest {
    private final String user = randomString();
    private final String password = randomString();

    private BlockchainProviderNonFungibleGateway toTest;

    @BeforeEach
    void setUp(WireMockRuntimeInfo runtime) {
        final BlockchainProviderGatewayClient gatewayClient = new BlockchainProviderGatewayClient(runtime.getHttpBaseUrl(),
                                                                                          new BasicAuthCredentials(
                                                                                          user,
                                                                                          password
                                                                                  ));
        toTest = new BlockchainProviderNonFungibleGateway(SecretType.IMX, gatewayClient);
    }

    @Test
    void retrievesNonFungibleAsset() {
        final String contractAddress = randomString();
        final String tokenId = randomString();
        final String url = "/api/assets/%s/%s".formatted(contractAddress, tokenId);
        stubFor(get(url).willReturn(jsonFromFile("/nonfungible/nonfungible-asset-response.json")));

        final NonFungibleAsset result = toTest.getNonFungible(contractAddress, tokenId);

        verifyRequestSentWithAuthorization(url, user, password);
        assertThat(result)
                .isEqualTo(aCryptoAssaultNonFungible());

    }

    @Test
    void retrievesNonFungibleAssetBalances() {
        final String walletAddress = randomString();
        final String contractAddress1 = randomString();
        final String contractAddress2 = randomString();
        final String url = "/api/assets?walletAddress=%s&contractAddresses=%s&contractAddresses=%s".formatted(walletAddress, contractAddress1, contractAddress2);
        stubFor(get(url).willReturn(jsonFromFile("/nonfungible/nonfungible-asset-balances-response.json")));

        final List<NonFungibleAssetBalance> result = toTest.listNonFungibles(walletAddress, contractAddress1, contractAddress2);

        verifyRequestSentWithAuthorization(url, user, password);
        assertThat(result)
                .containsExactlyInAnyOrder(
                        aGodsUnchainedNonFungibleAssetBalance(),
                        aCryptoAssaultNonFungibleAssetBalance()
                                          );
    }

    @Test
    void retrievesNonfungibleContract() {
        final String contractAddress = randomString();
        final String url = "/api/contracts/%s".formatted(contractAddress);
        stubFor(get(url).willReturn(jsonFromFile("/nonfungible/nonfungible-contract-response.json")));

        final NonFungibleContract result = toTest.getNonFungibleContract(contractAddress);

        verifyRequestSentWithAuthorization(url, user, password);
        assertThat(result)
                .isEqualTo(aCryptoAssaultNonFungibleContract());

    }
}