package network.arkane.provider.secret.generation;

import network.arkane.provider.chain.SecretType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecretGeneratorConfig {

    private List<SecretGenerator> generators;

    public SecretGeneratorConfig(final List<SecretGenerator> generators) {
        this.generators = generators;
    }

    @Bean
    public Map<SecretType, SecretGenerator> provideKeys() {
        return generators.stream()
                         .collect(Collectors.toMap(
                                 SecretGenerator::type,
                                 element -> element
                                                  ));
    }
}
