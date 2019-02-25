package network.arkane.provider.token;

import network.arkane.provider.gateway.GochainWeb3JGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NativeGochainTokenDiscoveryServiceTest {

    private NativeGochainTokenDiscoveryService nativeGochainTokenDiscoveryService;
    private GochainWeb3JGateway web3JGateway;

    @BeforeEach
    void setUp() {
        this.web3JGateway = mock(GochainWeb3JGateway.class);
        this.nativeGochainTokenDiscoveryService = new NativeGochainTokenDiscoveryService(web3JGateway);
    }

    @Test
    void getTokenInfo_noName() {
        final String tokenAddress = "0x0";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(null);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeGochainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noDecimals() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(null);
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeGochainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noSymbol() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(null);

        final Optional<TokenInfo> result = nativeGochainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(web3JGateway.getDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));
        when(web3JGateway.getName(tokenAddress)).thenReturn(tokenName);
        when(web3JGateway.getSymbol(tokenAddress)).thenReturn(tokenSymbol);

        final Optional<TokenInfo> result = nativeGochainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getAddress()).isEqualTo(tokenAddress);
        Assertions.assertThat(result.get().getName()).isEqualTo(tokenName);
        Assertions.assertThat(result.get().getSymbol()).isEqualTo(tokenSymbol);
        Assertions.assertThat(result.get().getDecimals()).isEqualTo(tokenDecimals);
        Assertions.assertThat(result.get().getType()).isEqualTo("ERC20");
    }
}