package network.arkane.provider.token;

import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;

public abstract class NativeEvmTokenDiscoveryService implements NativeTokenDiscoveryService {

    private EvmWeb3jGateway evmWeb3jGateway;

    public NativeEvmTokenDiscoveryService(EvmWeb3jGateway evmWeb3jGateway) {
        this.evmWeb3jGateway = evmWeb3jGateway;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = evmWeb3jGateway.getName(tokenAddress);
        final String symbol = evmWeb3jGateway.getSymbol(tokenAddress);
        final BigInteger decimals = evmWeb3jGateway.getDecimals(tokenAddress);

        if (name != null && decimals != null && symbol != null) {
            return Optional.of(TokenInfo.builder()
                                        .address(tokenAddress)
                                        .name(name)
                                        .decimals(decimals.intValue())
                                        .symbol(symbol)
                                        .type(getTokenType())
                                        .transferable(true)
                                        .build());
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    public abstract String getTokenType();

}
