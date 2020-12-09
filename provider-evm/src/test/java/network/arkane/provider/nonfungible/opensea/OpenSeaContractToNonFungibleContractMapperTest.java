package network.arkane.provider.nonfungible.opensea;

import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.NonFungibleContractTypeMapper;
import network.arkane.provider.opensea.OpenSeaContractToNonFungibleContractMapper;
import network.arkane.provider.opensea.domain.AssetContract;
import network.arkane.provider.opensea.domain.AssetMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class OpenSeaContractToNonFungibleContractMapperTest {

    private OpenSeaContractToNonFungibleContractMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OpenSeaContractToNonFungibleContractMapper(mock(NonFungibleContractTypeMapper.class));
    }

    @Test
    void map() {
        final AssetContract contract = AssetMother.aCryptoAssaultAsset().getAssetContract();

        final NonFungibleContract result = mapper.map(contract);

        assertThat(result.getName()).isEqualTo(contract.getName());
        assertThat(result.getDescription()).isEqualTo(contract.getDescription());
        assertThat(result.getAddress()).isEqualTo(contract.getAddress());
        assertThat(result.getSymbol()).isEqualTo(contract.getSymbol());
        assertThat(result.getUrl()).isEqualTo(contract.getExternalLink());
        assertThat(result.getImageUrl()).isEqualTo(contract.getImageUrl());
    }

    @Test
    void mapToList() {
        final AssetContract contract1 = AssetMother.aCryptoAssaultAsset().getAssetContract();
        final AssetContract contract2 = AssetContract.builder()
                                                     .address("da83c4d4-3a98-4881-b95c-5d877009dbbe")
                                                     .description("e8b10977-b3e0-4eb6-becb-fd9eae7c6786")
                                                     .externalLink("e650da24-de6d-4073-9b93-002e70732363")
                                                     .imageUrl("c17c4cb4-890a-467c-a6db-0da9bf114f8c")
                                                     .name("cc5be114-be84-4165-b578-a6af4b168abe")
                                                     .symbol("f3c4e4c4-46a7-4afd-a000-b392601d5855")
                                                     .build();

        final List<NonFungibleContract> result = mapper.mapToList(Arrays.asList(contract1, contract2));

        assertThat(result.get(0).getName()).isEqualTo(contract1.getName());
        assertThat(result.get(0).getDescription()).isEqualTo(contract1.getDescription());
        assertThat(result.get(0).getAddress()).isEqualTo(contract1.getAddress());
        assertThat(result.get(0).getSymbol()).isEqualTo(contract1.getSymbol());
        assertThat(result.get(0).getUrl()).isEqualTo(contract1.getExternalLink());
        assertThat(result.get(0).getImageUrl()).isEqualTo(contract1.getImageUrl());

        assertThat(result.get(1).getName()).isEqualTo(contract2.getName());
        assertThat(result.get(1).getDescription()).isEqualTo(contract2.getDescription());
        assertThat(result.get(1).getAddress()).isEqualTo(contract2.getAddress());
        assertThat(result.get(1).getSymbol()).isEqualTo(contract2.getSymbol());
        assertThat(result.get(1).getUrl()).isEqualTo(contract2.getExternalLink());
        assertThat(result.get(1).getImageUrl()).isEqualTo(contract2.getImageUrl());
    }
}
