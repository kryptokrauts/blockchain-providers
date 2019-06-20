package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.opensea.OpenSeaGateway;
import network.arkane.provider.opensea.domain.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EthereumNonFungibleGatewayTest {

    private EthereumNonFungibleGateway ethereumNonFungibleGateway;
    private OpenSeaGateway openSeaGateway;
    private OpenSeaAssetToNonFungibleAssetMapper mapper;

    @BeforeEach
    void setUp() {
        openSeaGateway = mock(OpenSeaGateway.class);
        mapper = mock(OpenSeaAssetToNonFungibleAssetMapper.class);
        this.ethereumNonFungibleGateway = new EthereumNonFungibleGateway(openSeaGateway, mapper);
    }

    @Test
    void getSecretType() {
        assertThat(this.ethereumNonFungibleGateway.getSecretType()).isEqualTo(SecretType.ETHEREUM);
    }

    @Test
    void listNonFungibles() {
        final String walletId = "gfhjafsfdgfhg";
        final List<Asset> openSeaTokens = new ArrayList<>();
        final ArrayList<NonFungibleAsset> expected = new ArrayList<>();

        when(openSeaGateway.listAssets(walletId)).thenReturn(openSeaTokens);
        when(mapper.mapToList(same(openSeaTokens))).thenReturn(expected);

        final List<NonFungibleAsset> result = this.ethereumNonFungibleGateway.listNonFungibles(walletId);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void listNonFungibles_withContractAddressesFilter() {
        final String walletId = "gfhjafsfdgfhg";
        final List<Asset> openSeaTokens = new ArrayList<>();
        final ArrayList<NonFungibleAsset> expected = new ArrayList<>();
        final String contractAddress1 = "afd`sd";
        final String contractAddress2 = "afdsgd";

        when(openSeaGateway.listAssets(walletId, contractAddress1, contractAddress2)).thenReturn(openSeaTokens);
        when(mapper.mapToList(same(openSeaTokens))).thenReturn(expected);

        final List<NonFungibleAsset> result = this.ethereumNonFungibleGateway.listNonFungibles(walletId, contractAddress1, contractAddress2);

        assertThat(result).isSameAs(expected);
    }
}
