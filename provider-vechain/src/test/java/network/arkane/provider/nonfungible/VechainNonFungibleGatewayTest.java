package network.arkane.provider.nonfungible;

import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VechainNonFungibleGatewayTest {

    private VechainNonFungibleGateway vechainNonFungibleGateway;
    private BusinessNonFungibleGateway businessNonFungibleGateway;


    @BeforeEach
    void setUp() {
        this.businessNonFungibleGateway = mock(BusinessNonFungibleGateway.class);
        this.vechainNonFungibleGateway = new VechainNonFungibleGateway(businessNonFungibleGateway);
    }

    @Test
    void listNonFungibles() {
        when(businessNonFungibleGateway.listNonFungibles(SecretType.VECHAIN, "walletAddress", "contractAddress")).thenReturn(Arrays.asList(mock(NonFungibleAsset.class)));

        List<NonFungibleAsset> assets = vechainNonFungibleGateway.listNonFungibles("walletAddress", "contractAddress");
        assertThat(assets).isNotEmpty();
    }

}
