package network.arkane.provider.tron.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import network.arkane.provider.token.NativeTokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NativeTronTokenDiscoveryService implements NativeTokenDiscoveryService {

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        throw new ChainNoLongerSupportedException();
    }

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }
}
