package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.VechainGateway;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class NativeVechainTokenDiscoveryService implements NativeTokenDiscoveryService {

    private VechainGateway vechainGateway;

    public NativeVechainTokenDiscoveryService(VechainGateway vechainGateway) {
        this.vechainGateway = vechainGateway;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = vechainGateway.getTokenName(tokenAddress);
        final String symbol = vechainGateway.getTokenSymbol(tokenAddress);
        final BigInteger decimals = vechainGateway.getTokenDecimals(tokenAddress);

        if (name != null && decimals != null && symbol != null) {
            return Optional.of(TokenInfo.builder()
                                        .address(tokenAddress)
                                        .name(name)
                                        .decimals(decimals.intValue())
                                        .symbol(symbol)
                                        .type("VIP180")
                                        .build());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public SecretType type() {
        return SecretType.VECHAIN;
    }
}
