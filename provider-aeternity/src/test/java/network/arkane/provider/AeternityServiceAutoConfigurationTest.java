package network.arkane.provider;

import network.arkane.provider.balance.AeternityBalanceGateway;
import network.arkane.provider.bridge.AeternityTransactionGateway;
import network.arkane.provider.sign.AeternitySpendTransactionSigner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        AeternityServiceAutoConfiguration.class,
        AeternitySpendTransactionSigner.class,
        AeternityTransactionGateway.class,
        AeternityBalanceGateway.class
})
@TestPropertySource("/configuration.properties")
public class AeternityServiceAutoConfigurationTest {

    @Autowired
    AeternityBalanceGateway aeternityBalanceGateway;

    @Autowired
    AeternitySpendTransactionSigner aeternitySpendTransactionSigner;

    @Autowired
    AeternityTransactionGateway aeternityTransactionGateway;

    @Test
    public void autoConfigurationTest() {
        Assertions.assertThat(aeternityBalanceGateway).isNotNull();
        Assertions.assertThat(aeternitySpendTransactionSigner).isNotNull();
        Assertions.assertThat(aeternityTransactionGateway).isNotNull();
    }
}
