package network.arkane.provider.token;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.Web3JGateway;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class NativeGochainTokenDiscoveryService implements NativeTokenDiscoveryService {

    private Web3JGateway web3JGateway;

    public NativeGochainTokenDiscoveryService(Web3JGateway web3JGateway) {
        this.web3JGateway = web3JGateway;
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = web3JGateway.getName(tokenAddress);
        final String symbol = web3JGateway.getSymbol(tokenAddress);
        final BigInteger decimals = web3JGateway.getDecimals(tokenAddress);

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
        return SecretType.GOCHAIN;
    }


}
