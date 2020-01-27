package network.arkane.provider.gateway;

import network.arkane.provider.clients.DeltaBalancesContractClient;
import network.arkane.provider.core.model.blockchain.NodeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VechainGatewayTest {
    private VechainGateway vechainGateway;

    @BeforeEach
    void setUp() {
        NodeProvider nodeProvider = NodeProvider.getNodeProvider();
        nodeProvider.setProvider("https://vechain-test.arkane.network");
        nodeProvider.setTimeout(10000);
        DeltaBalancesContractClient deltaBalancesContractClient = new DeltaBalancesContractClient("0xd8a100bccb8cb27ad7ef64d24e30949497e486aa");
        vechainGateway = new VechainGateway(deltaBalancesContractClient);
    }

    @Test
    void testTokenBalances() throws IOException {
        List<BigInteger> result = vechainGateway.getTokenBalances("0x297D6818F6801C49c7b82a4A328e11A3641199a7",
                                                                  Arrays.asList("0x0000000000000000000000000000456e65726779",
                                                                                "0x9c6e62b3334294d70c8e410941f52d482557955b"));

        assertThat(result).containsExactly(BigInteger.ZERO, BigInteger.ZERO);
    }
}
