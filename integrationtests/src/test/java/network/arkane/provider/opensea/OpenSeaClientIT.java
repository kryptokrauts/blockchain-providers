package network.arkane.provider.opensea;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.BlockProvidersIT;
import network.arkane.provider.opensea.domain.Assets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BlockProvidersIT.class)
class OpenSeaClientIT {

    @Autowired
    private OpenSeaClient client;

    @Test
    void listAssets() {
        final Assets assets = client.listAssets("0x0239769a1adf4def9f07da824b80b9c4fcb59593", new ArrayList<>());

        assertThat(assets.getAssets()).isNotEmpty();
    }

    @Test
    void listAssets_forSpecificContracts() {
        final String contract1 = "0xc1caf0c19a8ac28c41fe59ba6c754e4b9bd54de9";
        final String contract2 = "0xfac7bea255a6990f749363002136af6556b31e04";
        final Assets assets = client.listAssets("0x0239769a1adf4def9f07da824b80b9c4fcb59593", Arrays.asList(contract1, contract2));

        assertThat(assets.getAssets()).isNotEmpty();
        assets.getAssets().forEach((asset) -> assertThat(asset.getAssetContract().getAddress()).isIn(contract1, contract2));
    }
}



