package network.arkane.provider.aeternity;

import com.kryptokrauts.aeternity.sdk.constants.Network;
import com.kryptokrauts.aeternity.sdk.constants.VirtualMachine;
import com.kryptokrauts.aeternity.sdk.service.aeternity.AeternityServiceConfiguration;
import com.kryptokrauts.aeternity.sdk.service.aeternity.AeternityServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AeternityServiceAutoConfiguration {

  @Value("${network.arkane.aeternity.node.api.baseUrl:https://sdk-testnet.aepps.com}")
  String baseUrl;
  @Value("${network.arkane.aeternity.compiler.api.baseUrl:https://compiler.aepps.com}")
  String compilerBaseUrl;
  @Value("${network.arkane.aeternity.aeternal.api.baseUrl:https://testnet.aeternal.io}")
  String aeternalBaseUrl;
  @Value("${network.arkane.aeternity.network:TESTNET}")
  Network network;
  @Value("${network.arkane.aeternity.vm:FATE}")
  VirtualMachine targetVM;

  @Bean(name = "aeternity-configuration")
  public AeternityServiceConfiguration aeternityConfiguration() {
    return AeternityServiceConfiguration.configure()
        .baseUrl(baseUrl)
        .compilerBaseUrl(compilerBaseUrl)
        .aeternalBaseUrl(aeternalBaseUrl)
        .network(network)
        .targetVM(targetVM)
        .compile();
  }

  @Bean(name = "aeternity-service")
  public AeternityService aeternityService() {
    return new AeternityServiceFactory()
        .getService(aeternityConfiguration());
  }
}
