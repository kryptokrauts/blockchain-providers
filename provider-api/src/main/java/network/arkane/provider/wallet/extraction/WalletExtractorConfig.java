package network.arkane.provider.wallet.extraction;

import network.arkane.provider.wallet.extraction.request.ExtractionRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class WalletExtractorConfig {

    @Bean
    public Map<Class<? extends ExtractionRequest>, AbstractSecretExtractor<? extends ExtractionRequest>> provideInMap(final List<AbstractSecretExtractor<? extends
            ExtractionRequest>> abstractSecretExtractors) {
        return abstractSecretExtractors
                .stream()
                .collect(Collectors.toMap(AbstractSecretExtractor::getImportRequestType,
                                          Function.identity()));
    }
}