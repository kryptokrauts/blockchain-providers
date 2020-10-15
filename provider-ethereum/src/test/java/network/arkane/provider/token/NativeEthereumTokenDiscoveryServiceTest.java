package network.arkane.provider.token;

import network.arkane.provider.gateway.EthereumWeb3JGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NativeEthereumTokenDiscoveryServiceTest {

    private NativeEthereumTokenDiscoveryService nativeEthereumTokenDiscoveryService;
    private EthereumWeb3JGateway ethereumWeb3JGateway;

    @BeforeEach
    void setUp() {
        this.ethereumWeb3JGateway = mock(EthereumWeb3JGateway.class);
        this.nativeEthereumTokenDiscoveryService = new NativeEthereumTokenDiscoveryService(ethereumWeb3JGateway);
    }

    @Test
    void getTokenInfo_noName() {
        final String tokenAddress = "0x0";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(ethereumWeb3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(ethereumWeb3JGateway.getName(tokenAddress)).thenReturn(null);
        when(ethereumWeb3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeEthereumTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noDecimals() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";

        when(ethereumWeb3JGateway.getDecimals(tokenAddress)).thenReturn(null);
        when(ethereumWeb3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(ethereumWeb3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeEthereumTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noSymbol() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final int tokenDecimals = 15;

        when(ethereumWeb3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(ethereumWeb3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(ethereumWeb3JGateway.getSymbol(tokenAddress)).thenReturn(null);

        final Optional<TokenInfo> result = nativeEthereumTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(ethereumWeb3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(ethereumWeb3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(ethereumWeb3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeEthereumTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getAddress()).isEqualTo(tokenAddress);
        Assertions.assertThat(result.get().getName()).isEqualTo(tokenName);
        Assertions.assertThat(result.get().getSymbol()).isEqualTo(tokenSymbol);
        Assertions.assertThat(result.get().getDecimals()).isEqualTo(tokenDecimals);
        Assertions.assertThat(result.get().getType()).isEqualTo("ERC20");
    }
}
