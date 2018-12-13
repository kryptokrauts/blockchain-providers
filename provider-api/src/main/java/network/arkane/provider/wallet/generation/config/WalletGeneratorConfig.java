package network.arkane.provider.wallet.generation.config;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.generation.AbstractWalletGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class WalletGeneratorConfig {

    private List<AbstractWalletGenerator> walletGenerators;

    public WalletGeneratorConfig(List<AbstractWalletGenerator> walletGenerators) {
        this.walletGenerators = walletGenerators;
    }

    @Bean
    public Map<SecretType, AbstractWalletGenerator> provideMap() {
        return walletGenerators
                .stream()
                .collect(Collectors.toMap(
                        AbstractWalletGenerator::type,
                        e -> e));
    }

}
