package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.balance.domain.TokenBalanceMother;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.token.TokenInfoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EthereumBalanceGatewayTest {
    private EthereumBalanceGateway balanceGateway;
    private TokenDiscoveryService tokenDiscoveryService;
    private EthereumBalanceStrategy ethereumBalanceStrategy;

    @BeforeEach
    void setUp() {
        ethereumBalanceStrategy = mock(EthereumBalanceStrategy.class);
        when(ethereumBalanceStrategy.getBalance(any(String.class))).thenReturn(new BigInteger("1000000000000000000"));
        when(ethereumBalanceStrategy.getTokenBalance(any(String.class), any(String.class))).thenReturn(BigInteger.valueOf(1000000000000000000L));
        tokenDiscoveryService = mock(TokenDiscoveryService.class);

        balanceGateway = new EthereumBalanceGateway(tokenDiscoveryService, ethereumBalanceStrategy);
    }

    @Test
    void checkBalance() {
        Balance balance = balanceGateway.getBalance("address");
        assertThat(balance.getBalance()).isEqualTo(1);
        assertThat(balance.getRawBalance()).isEqualTo("1000000000000000000");
        assertThat(balance.getGasBalance()).isEqualTo(1);
        assertThat(balance.getRawGasBalance()).isEqualTo("1000000000000000000");
        assertThat(balance.getDecimals()).isEqualTo(18);
    }

    @Test
    void checkTokenBalance() {
        final TokenInfo fndTokenInfo = TokenInfoMother.fnd().build();
        final TokenBalance fndBalance = TokenBalanceMother.fndResult();

        when(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, fndTokenInfo.getAddress())).thenReturn(Optional.of(fndTokenInfo));
        when(ethereumBalanceStrategy.getTokenBalance("address", fndTokenInfo.getAddress())).thenReturn(new BigInteger(fndBalance.getRawBalance()));

        final TokenBalance result = balanceGateway.getTokenBalance("address", fndTokenInfo.getAddress());

        assertThat(result).isEqualTo(fndBalance);
    }


    @Test
    void getTokenBalances() {
        final TokenInfo fnd = TokenInfoMother.fnd().build();
        final TokenInfo zrx = TokenInfoMother.zrx().build();
        final TokenInfo dai = TokenInfoMother.dai().build();
        final TokenBalance fndBalance = TokenBalanceMother.fndResult();
        final TokenBalance zrxBalance = TokenBalanceMother.zrxResult();
        final TokenBalance daiBalance = TokenBalanceMother.daiResult();

        when(ethereumBalanceStrategy.getTokenBalances("address")).thenReturn(Arrays.asList(fndBalance, zrxBalance, daiBalance));
        when(tokenDiscoveryService.getTokens(SecretType.ETHEREUM)).thenReturn(Arrays.asList(fnd, zrx, dai));
        final List<TokenBalance> results = balanceGateway.getTokenBalances("address");
        assertThat(results).containsExactlyInAnyOrder(fndBalance, zrxBalance, daiBalance);
    }
}
