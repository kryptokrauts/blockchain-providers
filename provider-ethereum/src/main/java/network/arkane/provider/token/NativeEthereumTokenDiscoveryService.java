package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class NativeEthereumTokenDiscoveryService implements NativeTokenDiscoveryService {

    private EthereumWeb3JGateway ethereumWeb3JGateway;

    public NativeEthereumTokenDiscoveryService(EthereumWeb3JGateway ethereumWeb3JGateway) {
        this.ethereumWeb3JGateway = ethereumWeb3JGateway;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = ethereumWeb3JGateway.getName(tokenAddress);
        final String symbol = ethereumWeb3JGateway.getSymbol(tokenAddress);
        final BigInteger decimals = ethereumWeb3JGateway.getDecimals(tokenAddress);

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
        return SecretType.ETHEREUM;
    }


}
