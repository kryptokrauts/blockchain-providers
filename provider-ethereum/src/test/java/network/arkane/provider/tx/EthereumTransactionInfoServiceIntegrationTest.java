package network.arkane.provider.tx;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

class EthereumTransactionInfoServiceIntegrationTest {

    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private Web3j web3j;
    private EthereumTransactionInfoService ethereumTransactionInfoService;

    @BeforeEach
    void setUp() throws InterruptedException {
        web3j = Web3j.build(new HttpService("https://ethereum.arkane.network"));
        ethereumWeb3JGateway = new EthereumWeb3JGateway(web3j, "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC");
        Thread.sleep(100);
        ethereumTransactionInfoService = new EthereumTransactionInfoService(ethereumWeb3JGateway.web3());
    }

    @Test
    @Disabled
    void getFailed() {
        EvmTxInfo transaction = ethereumTransactionInfoService.getTransaction("0xc5178498b5c226d9f7e2f5086f72bf0e4f4d87e097c4e517f1bec128580fd537");

        assertThat(transaction.getFrom()).isEqualTo("0xc892a4dc36ffd6244d29f0cec1dd222eb92cfb71");
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.FAILED);
        assertThat(transaction.getConfirmations()).isNotZero();
    }

    @Test
    void getSuccessWithLogs() {
        EvmTxInfo transaction = ethereumTransactionInfoService.getTransaction("0xffb9f36e41023537595cf62f5ca1846c25f893c3c2d3d7c2806d75c43ff1483e");

        assertThat(transaction.getFrom()).isEqualTo("0xef985560bda2a9ae4ed92fc7f8ef7b5279e5d3fa");
        assertThat(transaction.getTo()).isEqualTo("0x174bfa6600bf90c885c7c01c7031389ed1461ab9");
        assertThat(transaction.getNonce()).isEqualTo(new BigInteger("1743"));
        assertThat(transaction.getGas()).isEqualTo(new BigInteger("116280"));
        assertThat(transaction.getGasUsed()).isEqualTo(new BigInteger("38123"));
        assertThat(transaction.getGasPrice()).isEqualTo(new BigInteger("20000000000"));
        assertThat(transaction.getHash()).isEqualTo("0xffb9f36e41023537595cf62f5ca1846c25f893c3c2d3d7c2806d75c43ff1483e");
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.SUCCEEDED);
        assertThat(transaction.getConfirmations()).isNotZero();
        assertThat(transaction.getBlockHash()).isEqualTo("0x25049b9f5db6cc40c2dbd7a4ecda0f5b539caa726af02e923ec1a9c4ee8ebc51");
        assertThat(transaction.getBlockNumber()).isEqualTo(new BigInteger("8064684"));

        assertThat(transaction.getLogs()).hasSize(1);
        assertThat(transaction.getLogs().get(0).getLogIndex()).isEqualTo(new BigInteger("132"));
        assertThat(transaction.getLogs().get(0).getData()).isEqualTo("0x0000000000000000000000000000000000000000000000006935c9d8a109f000");
        assertThat(transaction.getLogs().get(0).getTopics()).containsExactly("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
                                                                             "0x000000000000000000000000ef985560bda2a9ae4ed92fc7f8ef7b5279e5d3fa",
                                                                             "0x000000000000000000000000a33fa70ff75ad1235947563d16fb98ccb1d2df95");
    }

    @Test
    @Disabled
    void getFromOtherEndpoint() {
        Map<String, Object> parameters = Collections.singletonMap("endpoint", "https://rinkeby.arkane.network");

        EvmTxInfo transaction = ethereumTransactionInfoService.getTransaction("0x3ea246bb6c7cfb8d3899addeddf4135ad0d716b041b7018b8d1b39951ee54c66", parameters);

        assertThat(transaction.getFrom()).isEqualTo("0x080959e98c5afc1709f9e4d716ac11be2a850b73");
    }
}
