package network.arkane.provider.gateway;

import network.arkane.provider.gas.EvmEstimateGasResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Disabled Test Because it fails during build for unknown reason")
class EthereumWeb3JGatewayTest {

    private static final String DELTA_BALANCES_ADDRESS = "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC";
    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private Web3j web3j;

    @BeforeEach
    void setUp() throws InterruptedException {
        web3j = Web3j.build(new HttpService("https://ethereum.arkane.network"));
        ethereumWeb3JGateway = new EthereumWeb3JGateway(web3j, DELTA_BALANCES_ADDRESS);
        Thread.sleep(100);
    }

    @Test
    void web3j() {
        assertThat(ethereumWeb3JGateway.web3()).isEqualTo(web3j);
    }

    @Test
    void getAddress() {
        Optional<String> result = ethereumWeb3JGateway.getAddressForEnsName("mewtopia.eth");

        assertThat(result).isPresent().contains("0xdecaf9cd2367cdbb726e904cd6397edfcae6068d");
    }

    @Test
    void getTokenBalances() {
        List<BigInteger> tokenBalances = ethereumWeb3JGateway.getTokenBalances("0x742d35Cc6634C0532925a3b844Bc454e4438f44e", Arrays.asList("0x0d8775f648430679a709e98d2b0cb6250d2887ef"));
        assertThat(tokenBalances).hasSize(1);
    }

    @Test
    void estimateGas() {
        EvmEstimateGasResult result = ethereumWeb3JGateway.estimateGas("0x1fb53eb183b86582176f6aa8f4db72b62caf0d4b",
                                                                       "0x3845badade8e6dff049820680d1f14bd3903a5d0",
                                                                       BigInteger.ZERO,
                                                                       "0xa9059cbb0000000000000000000000000943a1a6a92d25eeb6efe7a47d81a5bc0d2ae1430000000000000000000000000000000000000000000000000de0b6b3a7640000");
        assertThat(result.isReverted()).isFalse();
    }

    @Test
    void estimateGasReverts() {
        EvmEstimateGasResult result = ethereumWeb3JGateway.estimateGas("0x9ca4e34ad23a3b177bd0d6e48149efd0852a7752",
                                                                       "0x89d24a6b4ccb1b6faa2625fe562bdd9a23260359",
                                                                       BigInteger.ZERO,
                                                                       "0xa9059cbb000000000000000000000000107af532e6f828da6fe79699123c9a5ea0123d160000000000000000000000000000000000000000000000000de0b9f291777780");
        assertThat(result.getGasLimit()).isEqualTo(new BigInteger("200000"));
        assertThat(result.isReverted()).isTrue();
    }

    @Test
    void getNextNonce() {
        BigInteger result = ethereumWeb3JGateway.getNextNonce("0xf8d911b0E4292dB5eA95162460e98e0edE953b98");

        assertThat(result).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void getApprovalBalance() {
        BigInteger result = ethereumWeb3JGateway.getApprovalBalance("0xef1d6c35a3877ec20a72fe3a2df79cdbaee39148",
                                                                    "0x4f9bebe3adc3c7f647c0023c60f91ac9dffa52d5",
                                                                    "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984");

        assertThat(result).isEqualTo(BigInteger.ZERO);
    }

    @Test
    void getName() {
        ethereumWeb3JGateway.getEnsName("0xdecaf9cd2367cdbb726e904cd6397edfcae6068d");
    }

    @Test
    void ethGasPrice() {
        EthGasPrice result = ethereumWeb3JGateway.ethGasPrice();

        assertThat(result).isNotNull();
    }
}
