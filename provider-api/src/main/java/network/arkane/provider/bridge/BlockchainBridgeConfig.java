package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BlockchainBridgeConfig {

    @Bean
    public Map<SecretType, BlockchainBridge> provideBridges(final List<BlockchainBridge> bridges) {
        return bridges
                .stream()
                .collect(Collectors.toMap(BlockchainBridge::getType,
                                          Function.identity()));
    }
}
