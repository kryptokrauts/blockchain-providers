package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.OpenSeaGateway;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetContract;
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
    private OpenSeaContractToNonFungibleContractMapper contractMapper;

    @BeforeEach
    void setUp() {
        openSeaGateway = mock(OpenSeaGateway.class);
        mapper = mock(OpenSeaAssetToNonFungibleAssetMapper.class);
        contractMapper = mock(OpenSeaContractToNonFungibleContractMapper.class);
        ethereumNonFungibleGateway = new EthereumNonFungibleGateway(openSeaGateway, mapper, contractMapper);
    }

    @Test
    void getSecretType() {
        assertThat(ethereumNonFungibleGateway.getSecretType()).isEqualTo(SecretType.ETHEREUM);
    }

    @Test
    void listNonFungibles() {
        final String walletId = "gfhjafsfdgfhg";
        final List<Asset> openSeaTokens = new ArrayList<>();
        final ArrayList<NonFungibleAsset> expected = new ArrayList<>();

        when(openSeaGateway.listAssets(walletId)).thenReturn(openSeaTokens);
        when(mapper.mapToList(same(openSeaTokens))).thenReturn(expected);

        final List<NonFungibleAsset> result = ethereumNonFungibleGateway.listNonFungibles(walletId);

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

        final List<NonFungibleAsset> result = ethereumNonFungibleGateway.listNonFungibles(walletId, contractAddress1, contractAddress2);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void getNonFungible() {
        final String contractAddress = "afdsgd";
        final String tokenId = "131";
        final Asset openSeaToken = Asset.builder().build();
        final NonFungibleAsset expected = NonFungibleAsset.builder().build();

        when(openSeaGateway.getAsset(contractAddress, tokenId)).thenReturn(openSeaToken);
        when(mapper.map(same(openSeaToken))).thenReturn(expected);

        final NonFungibleAsset result = ethereumNonFungibleGateway.getNonFungible(contractAddress, tokenId);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void getNonFungibleContract() {
        final String contractAddress = "42d91cf8-c41a-4285-8aa8-fdf3082882eb";
        final AssetContract assetContract = mock(AssetContract.class);
        final NonFungibleContract nonFungibleContract = mock(NonFungibleContract.class);

        when(openSeaGateway.getContract(contractAddress)).thenReturn(assetContract);
        when(contractMapper.map(assetContract)).thenReturn(nonFungibleContract);

        final NonFungibleContract result = ethereumNonFungibleGateway.getNonFungibleContract(contractAddress);

        assertThat(result).isSameAs(nonFungibleContract);
    }
}
