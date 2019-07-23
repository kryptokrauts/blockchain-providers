package network.arkane.provider;

import com.kryptokrauts.aeternity.sdk.constants.Network;
import com.kryptokrauts.aeternity.sdk.service.ServiceConfiguration;
import com.kryptokrauts.aeternity.sdk.service.account.AccountService;
import com.kryptokrauts.aeternity.sdk.service.account.AccountServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.chain.ChainService;
import com.kryptokrauts.aeternity.sdk.service.chain.ChainServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.compiler.CompilerService;
import com.kryptokrauts.aeternity.sdk.service.compiler.CompilerServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionService;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionServiceConfiguration;
import com.kryptokrauts.aeternity.sdk.service.transaction.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AeternityServiceAutoConfiguration {

  @Bean(name = "aeternity-accountService")
  public AccountService accountService(
      final @Value("${network.arkane.aeternity.api.baseUrl}") String baseUrl) {
    return new AccountServiceFactory()
        .getService(ServiceConfiguration.configure().baseUrl(baseUrl).compile());
  }

  @Bean(name = "aeternity-transactionService")
  public TransactionService transactionService(
      final @Value("${network.arkane.aeternity.api.baseUrl}") String baseUrl,
      final @Value("${network.arkane.aeternity.network}") Network network) {
    return new TransactionServiceFactory().getService(
        TransactionServiceConfiguration.configure().baseUrl(baseUrl).network(network).compile());
  }

  @Bean(name = "aeternity-chainService")
  public ChainService chainService(
      final @Value("${network.arkane.aeternity.api.baseUrl}") String baseUrl) {
    return new ChainServiceFactory()
        .getService(ServiceConfiguration.configure().baseUrl(baseUrl).compile());
  }

  @Bean(name = "aeternity-compilerService")
  public CompilerService compilerService(
      final @Value("${network.arkane.aeternity.compiler.api.baseUrl}") String baseUrl) {
    return new CompilerServiceFactory()
        .getService(ServiceConfiguration.configure().baseUrl(baseUrl).compile());
  }
}
