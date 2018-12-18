package network.arkane.provider.token;

import network.arkane.provider.gateway.VechainGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NativeVechainTokenDiscoveryServiceTest {

    private NativeVechainTokenDiscoveryService nativeVechainTokenDiscoveryService;
    private VechainGateway vechainGateway;

    @BeforeEach
    void setUp() {
        this.vechainGateway = mock(VechainGateway.class);
        this.nativeVechainTokenDiscoveryService = new NativeVechainTokenDiscoveryService(vechainGateway);
    }

    @Test
    void getTokenInfo() {
        final String tokenAddress = "0x0";
        final String tokenName = "Some Token";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 13;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = nativeVechainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getAddress()).isEqualTo(tokenAddress);
        Assertions.assertThat(result.get().getName()).isEqualTo(tokenName);
        Assertions.assertThat(result.get().getSymbol()).isEqualTo(tokenSymbol);
        Assertions.assertThat(result.get().getDecimals()).isEqualTo(tokenDecimals);
        Assertions.assertThat(result.get().getType()).isEqualTo("VIP180");
    }

    @Test
    void getTokenInfo_noName() {
        final String tokenAddress = "0x0";
        final String tokenSymbol = "STN";
        final int tokenDecimals = 15;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(null);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = nativeVechainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noDecimals() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final String tokenSymbol = "STN";

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(tokenSymbol);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(null);

        final Optional<TokenInfo> result = nativeVechainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTokenInfo_noSymbol() {
        final String tokenAddress = "0x0";
        final String tokenName = "SomeToken";
        final int tokenDecimals = 15;

        when(vechainGateway.getTokenName(tokenAddress)).thenReturn(tokenName);
        when(vechainGateway.getTokenSymbol(tokenAddress)).thenReturn(null);
        when(vechainGateway.getTokenDecimals(tokenAddress)).thenReturn(new BigInteger(String.valueOf(tokenDecimals)));

        final Optional<TokenInfo> result = nativeVechainTokenDiscoveryService.getTokenInfo(tokenAddress);

        Assertions.assertThat(result).isEmpty();
    }
}