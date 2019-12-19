package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.MaticWeb3JGateway;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class NativeMaticTokenDiscoveryService implements NativeTokenDiscoveryService {

    private MaticWeb3JGateway maticWeb3JGateway;

    public NativeMaticTokenDiscoveryService(MaticWeb3JGateway maticWeb3JGateway) {
        this.maticWeb3JGateway = maticWeb3JGateway;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = maticWeb3JGateway.getName(tokenAddress);
        final String symbol = maticWeb3JGateway.getSymbol(tokenAddress);
        final BigInteger decimals = maticWeb3JGateway.getDecimals(tokenAddress);

        if (name != null && decimals != null && symbol != null) {
            return Optional.of(TokenInfo.builder()
                                        .address(tokenAddress)
                                        .name(name)
                                        .decimals(decimals.intValue())
                                        .symbol(symbol)
                                        .type("ERC20")
                                        .build());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }
}
