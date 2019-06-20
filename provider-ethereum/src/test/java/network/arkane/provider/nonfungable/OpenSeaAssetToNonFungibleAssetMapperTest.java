package network.arkane.provider.nonfungable;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetMother;
import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OpenSeaAssetToNonFungibleAssetMapperTest {

    private OpenSeaAssetToNonFungibleAssetMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new OpenSeaAssetToNonFungibleAssetMapper();
    }

    @Test
    void map() {
        final Asset asset = AssetMother.aCryptoAssaultAsset();
        final NonFungibleAsset expected = NonFungibleAssetMother.aCryptoAssaultNonFungible();

        final NonFungibleAsset result = this.mapper.map(asset);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void map_noOwner() {
        final Asset asset = AssetMother.aCryptoAssaultAssetBuilder().owner(null).build();
        final NonFungibleAsset expected = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().owner(null).build();

        final NonFungibleAsset result = this.mapper.map(asset);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void mapList() {
        final Asset asset1 = AssetMother.aCryptoAssaultAsset();
        final Asset asset2 = AssetMother.aCryptoAssaultAssetBuilder().owner(null).build();
        final NonFungibleAsset expected1 = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().build();
        final NonFungibleAsset expected2 = NonFungibleAssetMother.aCryptoAssaultNonFungibleBuilder().owner(null).build();

        final List<NonFungibleAsset> result = this.mapper.mapToList(Arrays.asList(asset1, asset2));

        assertThat(result).containsExactly(expected1, expected2);
    }
}
