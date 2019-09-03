package network.arkane.provider.nonfungable;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetMother;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenSeaAssetToNonFungibleAssetMapperTest {

    private OpenSeaAssetToNonFungibleAssetMapper mapper;
    private OpenSeaContractToNonFungibleContractMapper contractMapper;

    @BeforeEach
    void setUp() {
        contractMapper = mock(OpenSeaContractToNonFungibleContractMapper.class);
        this.mapper = new OpenSeaAssetToNonFungibleAssetMapper(contractMapper);
    }

    @Test
    void map() {
        final Asset asset = AssetMother.aCryptoAssaultAsset();
        final NonFungibleContract contract = mock(NonFungibleContract.class);
        final NonFungibleAsset expected = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().contract(contract).build();

        when(contractMapper.map(asset.getAssetContract())).thenReturn(contract);

        final NonFungibleAsset result = this.mapper.map(asset);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void map_noOwner() {
        final Asset asset = AssetMother.aCryptoAssaultAssetBuilder().owner(null).build();
        final NonFungibleContract contract = mock(NonFungibleContract.class);
        final NonFungibleAsset expected = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().contract(contract).owner(null).build();

        when(contractMapper.map(asset.getAssetContract())).thenReturn(contract);

        final NonFungibleAsset result = this.mapper.map(asset);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void mapList() {
        final Asset asset1 = AssetMother.aCryptoAssaultAsset();
        final Asset asset2 = AssetMother.aCryptoAssaultAssetBuilder().owner(null).build();
        final NonFungibleContract contract = mock(NonFungibleContract.class);
        final NonFungibleAsset expected1 = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().contract(contract).build();
        final NonFungibleAsset expected2 = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().contract(contract).owner(null).build();

        when(contractMapper.map(asset1.getAssetContract())).thenReturn(contract);
        when(contractMapper.map(asset2.getAssetContract())).thenReturn(contract);

        final List<NonFungibleAsset> result = this.mapper.mapToList(Arrays.asList(asset1, asset2));

        assertThat(result).containsExactly(expected1, expected2);
    }
}
