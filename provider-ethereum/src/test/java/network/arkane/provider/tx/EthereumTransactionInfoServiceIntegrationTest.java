package network.arkane.provider.tx;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

import static org.assertj.core.api.Java6Assertions.assertThat;

class EthereumTransactionInfoServiceIntegrationTest {

    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private Web3j web3j;
    private EthereumTransactionInfoService ethereumTransactionInfoService;

    @BeforeEach
    void setUp() throws InterruptedException {
        web3j = Web3j.build(new HttpService("https://ethereum.arkane.network"));
        ethereumWeb3JGateway = new EthereumWeb3JGateway(web3j, "0xb9b604f149e72b1f09f5943f6a403c09afcc0eb31ba5546b294fd6fe8d234e19");
        Thread.sleep(100);
        ethereumTransactionInfoService = new EthereumTransactionInfoService(ethereumWeb3JGateway);
    }

    @Test
    void getFailed() {
        EthereumTxInfo transaction = ethereumTransactionInfoService.getTransaction("0x67ec3acc5274a88c50d1e79e9b9d4c2c3d5e0e3ba3cc33b32d65f3fdb3b5a258");

        assertThat(transaction.getFrom()).isEqualTo("0xfbd28a75d7593cc9b934878673a1bfc13831ae6f");
        assertThat(transaction.getTo()).isEqualTo("0xc6725ae749677f21e4d8f85f41cfb6de49b9db29");
        assertThat(transaction.getGas()).isEqualTo(new BigInteger("667749"));
        assertThat(transaction.getGasUsed()).isEqualTo(new BigInteger("441825"));
        assertThat(transaction.getGasPrice()).isEqualTo(new BigInteger("5000000000"));
        assertThat(transaction.getLogs()).isEmpty();
        assertThat(transaction.getHash()).isEqualTo("0x67ec3acc5274a88c50d1e79e9b9d4c2c3d5e0e3ba3cc33b32d65f3fdb3b5a258");
        assertThat(transaction.getStatus()).isEqualTo(TxStatus.FAILED);
        assertThat(transaction.getConfirmations()).isNotZero();
        assertThat(transaction.getBlockHash()).isEqualTo("0xa120e48f33f960b1c3c2e6c5a1c1326e73064596005c76889d846854520bcfe0");
        assertThat(transaction.getBlockNumber()).isEqualTo(new BigInteger("5602146"));

    }

    @Test
    void getSuccessWithLogs() {
        EthereumTxInfo transaction = ethereumTransactionInfoService.getTransaction("0xffb9f36e41023537595cf62f5ca1846c25f893c3c2d3d7c2806d75c43ff1483e");

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
        assertThat(transaction.getLogs().get(0).getType()).isEqualTo("mined");
        assertThat(transaction.getLogs().get(0).getData()).isEqualTo("0x0000000000000000000000000000000000000000000000006935c9d8a109f000");
        assertThat(transaction.getLogs().get(0).getTopics()).containsExactly("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef",
                                                                             "0x000000000000000000000000ef985560bda2a9ae4ed92fc7f8ef7b5279e5d3fa",
                                                                             "0x000000000000000000000000a33fa70ff75ad1235947563d16fb98ccb1d2df95");
    }
}