package network.arkane.provider.opensea;

import network.arkane.provider.opensea.domain.Asset;
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
}
