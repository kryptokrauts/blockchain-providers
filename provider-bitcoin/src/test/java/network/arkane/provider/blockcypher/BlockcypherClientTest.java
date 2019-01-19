package network.arkane.provider.blockcypher;

import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionRequest;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class BlockcypherClientTest {

    private static final String USER_AGENT = "curl/7.54.0";
    private static BlockcypherClient client;
    private static String TOKEN = "816540806ec340e8aedca3ec3d176258";

    @Configuration
    @Component
    @EnableFeignClients(clients = {BlockcypherClient.class})
    @ImportAutoConfiguration( {RibbonAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
    public static class TestContext {

    }


    @BeforeAll
    public static void onSetup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestContext.class);
        context.setAllowBeanDefinitionOverriding(false);
        client = context.getBean(BlockcypherClient.class);

    }

    @Test
    void getBalance() throws IOException {
        String balanceStr = client.getBalance(USER_AGENT, "btc", "test3", TOKEN, "mwN1cqMbvLacFY6PuKR9whUp1cCktg9DNt");

        BlockcypherAddress balance = new ObjectMapper().readValue(balanceStr, BlockcypherAddress.class);
        assertThat(balance.getAddress()).isEqualTo("mwN1cqMbvLacFY6PuKR9whUp1cCktg9DNt");
    }

    @Test
    void getUnspentTransactions() throws IOException {
        String result = client.getUnspents(USER_AGENT, "btc", "test3", TOKEN, "mwN1cqMbvLacFY6PuKR9whUp1cCktg9DNt");
        BlockcypherAddressUnspents unspents = new ObjectMapper().readValue(result, BlockcypherAddressUnspents.class);
        assertThat(unspents.getAddress()).isEqualTo("mwN1cqMbvLacFY6PuKR9whUp1cCktg9DNt");
        assertThat(unspents.getTransactionRefs()).isNotEmpty();
    }

    @Test
    void send() throws IOException {
        BlockCypherRawTransactionRequest request = new BlockCypherRawTransactionRequest(
                "01000000016c4f9fd0290b9e4c50b6f84544002fe6aaeac10fb377544dc274c7c3f4921b9b000000008a473044022039ff93a8af80cdc589a3c6dade2c57563cc14dcbfab1b7e95f71b01539c1032d022014aad0562835aa369c2766115708448b0487f9f0d7236342d3e7de0cc434114d81410496e59446aed552e60fb29b6a9c6c71d7c1b38b8396e67d9940dbb867a70e314c591b20a0e59e36259439e1d0b6ae16975ca7097103d8d1ac987a02d121b5cd16ffffffff0201000000000000001976a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588acc8edd900000000001976a91464d12bfa319ec47fe040bf6d2dbb3013d85f552588ac00000000");
        String result = client.sendSignedTransaction(USER_AGENT, "btc", "test3", TOKEN, request);

        System.out.println(result);
//        BlockCypherRawTransactionResponse response = new ObjectMapper().readValue(result, BlockCypherRawTransactionResponse.class);
    }
}