package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;

import java.util.Optional;

public interface NativeTokenDiscoveryService {
    Optional<TokenInfo> getTokenInfo(String tokenAddress);

    SecretType type();
}
