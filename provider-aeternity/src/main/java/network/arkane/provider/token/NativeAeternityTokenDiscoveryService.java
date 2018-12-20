package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NativeAeternityTokenDiscoveryService implements NativeTokenDiscoveryService {

    @Override
    public Optional<TokenInfo> getTokenInfo(String tokenAddress) {
        // TODO
        return Optional.empty();
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
