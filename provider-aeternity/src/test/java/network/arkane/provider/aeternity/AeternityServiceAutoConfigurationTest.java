package network.arkane.provider.aeternity;

import com.kryptokrauts.aeternity.sdk.constants.Network;
import com.kryptokrauts.aeternity.sdk.constants.VirtualMachine;
import com.kryptokrauts.aeternity.sdk.service.aeternity.AeternityServiceConfiguration;
import network.arkane.provider.aeternity.balance.AeternityBalanceGateway;
import network.arkane.provider.aeternity.bridge.AeternityTransactionGateway;
import network.arkane.provider.aeternity.sign.AeternitySpendTransactionSigner;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestPropertySource("/configuration.properties")
public class AeternityServiceAutoConfigurationTest {

  @Autowired
  AeternityBalanceGateway aeternityBalanceGateway;

  @Autowired
  AeternitySpendTransactionSigner aeternitySpendTransactionSigner;

  @Autowired
  AeternityTransactionGateway aeternityTransactionGateway;

  @Autowired
  AeternityServiceConfiguration aeternityServiceConfiguration;

  @Configuration
  @ComponentScan("network.arkane.provider.aeternity")
  public static class ComponentScanConfig {

  }

  @Test
  public void autoConfigurationTest() {
    Assertions.assertThat(aeternityBalanceGateway).isNotNull();
    Assertions.assertThat(aeternitySpendTransactionSigner).isNotNull();
    Assertions.assertThat(aeternityTransactionGateway).isNotNull();
    Assertions.assertThat(Network.TESTNET).isEqualTo(aeternityServiceConfiguration.getNetwork());
    Assertions.assertThat(VirtualMachine.FATE)
        .isEqualTo(aeternityServiceConfiguration.getTargetVM());
  }
}
