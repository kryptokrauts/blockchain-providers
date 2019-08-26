package network.arkane.provider.opensea;

import network.arkane.provider.opensea.domain.Asset;
import network.arkane.provider.opensea.domain.AssetContract;
import network.arkane.provider.opensea.domain.Assets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenSeaGatewayTest {

    private OpenSeaGateway gateway;
    private long maxRequestsPerSecond;
    private OpenSeaClient client;

    @BeforeEach
    void setUp() {
        this.client = mock(OpenSeaClient.class);
        this.maxRequestsPerSecond = 100L;
        gateway = new OpenSeaGateway(this.client, this.maxRequestsPerSecond);
    }

    @Test
    void listAssets() {
        final String owner = "gshdfg";
        final Asset asset1 = mock(Asset.class);
        final Asset asset2 = mock(Asset.class);

        when(client.listAssets(owner, new ArrayList<>())).thenReturn(Assets.builder().assets(Arrays.asList(asset1, asset2)).build());

        final List<Asset> result = gateway.listAssets(owner);

        assertThat(result).containsOnly(asset1, asset2);
    }

    @Test
    void listAssets_withContractAddresses() {
        final String owner = "gshdfg";
        final String[] contractAddresses = {"fgdhj", "traesytrdytfu"};
        final Asset asset1 = mock(Asset.class);
        final Asset asset2 = mock(Asset.class);

        when(client.listAssets(owner, Arrays.asList(contractAddresses))).thenReturn(Assets.builder().assets(Arrays.asList(asset1, asset2)).build());

        final List<Asset> result = gateway.listAssets(owner, contractAddresses);

        assertThat(result).containsOnly(asset1, asset2);
    }

    @Test
    void listAssets_contractAddressesNull() {
        final String owner = "gshdfg";
        final Asset asset1 = mock(Asset.class);
        final Asset asset2 = mock(Asset.class);

        when(client.listAssets(owner, new ArrayList<>())).thenReturn(Assets.builder().assets(Arrays.asList(asset1, asset2)).build());

        final List<Asset> result = gateway.listAssets(owner, null);

        assertThat(result).containsOnly(asset1, asset2);
    }

    @Test
    void getAsset() {
        final Asset asset = mock(Asset.class);

        when(client.getAsset("0x12345", "123")).thenReturn(asset);

        final Asset result = gateway.getAsset("0x12345", "123");

        assertThat(result).isEqualTo(asset);
    }

    @Test
    void getContract() {
        final String contractAddress = "1cf22539-df42-4ae2-be8d-e43f7c24c9ef";
        final AssetContract expected = mock(AssetContract.class);

        when(client.getContract(contractAddress)).thenReturn(expected);

        final AssetContract result = gateway.getContract(contractAddress);

        assertThat(result).isSameAs(expected);
    }
}
