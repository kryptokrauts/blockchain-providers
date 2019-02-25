package network.arkane.provider.litecoin;


import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LitecoinAutoConfigurationTest {

    @Test
    void setsEnv() {
        LitecoinEnv litecoinEnv = new LitecoinAutoConfiguration().litecoinEnv();

        assertThat(litecoinEnv.getNetwork()).isEqualTo(Network.LITECOIN);
        assertThat(litecoinEnv.getNetworkParameters()).isEqualTo(new LitecoinParams());
    }
}