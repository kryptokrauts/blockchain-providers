package network.arkane.provider.opensea;

import network.arkane.provider.opensea.domain.Assets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class OpenSeaClientTest {

    private static OpenSeaClient client;

    @Configuration
    @Component
    @EnableFeignClients(clients = {OpenSeaClient.class})
    @ImportAutoConfiguration(value = {RibbonAutoConfiguration.class,
                                      FeignRibbonClientAutoConfiguration.class,
                                      FeignAutoConfiguration.class,
                                      HttpMessageConvertersAutoConfiguration.class})
    public static class TestContext {}

    @BeforeAll
    static void onSetup() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestContext.class);
        context.setAllowBeanDefinitionOverriding(false);
        client = context.getBean(OpenSeaClient.class);

    }

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



