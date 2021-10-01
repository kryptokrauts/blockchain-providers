package network.arkane.provider.nonfungible;

import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class VechainNonFungibleGatewayTest {

    private VechainNonFungibleGateway vechainNonFungibleGateway;


    @BeforeEach
    void setUp() {
        this.vechainNonFungibleGateway = new VechainNonFungibleGateway();
    }

    @Test
    void listNonFungibles() {
        //   when(businessNonFungibleGateway.listNonFungibles(SecretType.VECHAIN, "walletAddress", "contractAddress")).thenReturn(Arrays.asList(mock(NonFungibleAsset.class)));

        List<NonFungibleAssetBalance> assets = vechainNonFungibleGateway.listNonFungibles("walletAddress", "contractAddress");
        assertThat(assets).isNotEmpty();
    }

}
