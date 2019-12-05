package network.arkane.provider.nonfungible;

import network.arkane.provider.business.token.BusinessTokenGateway;
import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import network.arkane.provider.business.token.model.TokenType;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VechainNonFungibleGatewayTest {

    private VechainNonFungibleGateway vechainNonFungibleGateway;
    private BusinessTokenGateway businessTokenGateway;


    @BeforeEach

    void setUp() {
        this.businessTokenGateway = mock(BusinessTokenGateway.class);
        this.vechainNonFungibleGateway = new VechainNonFungibleGateway(businessTokenGateway);
    }

    @Test
    void listNonFungibles() {
        when(businessTokenGateway.getTokensForAddress("walletAddress")).thenReturn(Arrays.asList(getTokenDto()));

        List<NonFungibleAsset> assets = vechainNonFungibleGateway.listNonFungibles("walletAddress", "contractAddress");
        assertThat(assets).isNotEmpty();
    }

    private TokenDto getTokenDto() {
        return new TokenDto(
                SecretType.AETERNITY,
                "contract_address",
                BigInteger.ONE,
                getTokenType(),
                BigInteger.ONE,
                "imageUrl",
                "imagePreviewUrl",
                "imageThumbnailurl",
                "url",
                "backgroundColor"
        );
    }

    private TokenType getTokenType() {
        return new TokenType(1L, "tokenType", "description", 8, BigInteger.ONE,
                "tx_hash",
                new Date(),
                true,
                new Date(),
                getTokenContract(),
                true,
                "properties", "url", "backgroundColor");
    }

    private TokenContract getTokenContract() {
        return new TokenContract(1L, "address", true, new Date(), new Date(), "description",
                "application_id", "name", "transactionhash", "ower");
    }
}