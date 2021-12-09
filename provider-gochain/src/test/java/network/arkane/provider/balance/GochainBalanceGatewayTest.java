package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.balance.domain.TokenBalanceMother;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.GochainWeb3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.token.TokenInfoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GochainBalanceGatewayTest {
    private GochainBalanceGateway balanceGateway;
    private GochainWeb3JGateway web3JGateway;
    private TokenDiscoveryService tokenDiscoveryService;

    @BeforeEach
    void setUp() {
        web3JGateway = mock(GochainWeb3JGateway.class);
        EthGetBalance balance = mock(EthGetBalance.class);
        when(balance.getBalance()).thenReturn(new BigInteger("1000000000000000000"));
        when(web3JGateway.getBalance(any(String.class))).thenReturn(balance);
        when(web3JGateway.getTokenBalance(any(String.class), any(String.class))).thenReturn(BigInteger.valueOf(1000000000000000000L));
        tokenDiscoveryService = mock(TokenDiscoveryService.class);

        balanceGateway = new GochainBalanceGateway(web3JGateway, tokenDiscoveryService);
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

        when(tokenDiscoveryService.getTokenInfo(SecretType.GOCHAIN, fndTokenInfo.getAddress())).thenReturn(Optional.of(fndTokenInfo));
        when(web3JGateway.getTokenBalance("address", fndTokenInfo.getAddress())).thenReturn(new BigInteger(fndBalance.getRawBalance()));

        final List<TokenBalance> result = balanceGateway.getTokenBalances("address", Arrays.asList(fndTokenInfo.getAddress()));

        assertThat(result).containsOnly(fndBalance);
    }

    @Test
    void getTokenBalances() {
        final TokenInfo fnd = TokenInfoMother.fnd().build();
        final TokenInfo zrx = TokenInfoMother.zrx().build();
        final TokenInfo dai = TokenInfoMother.dai().build();
        final TokenBalance fndBalance = TokenBalanceMother.fndResult();
        final TokenBalance zrxBalance = TokenBalanceMother.zrxResult();
        final TokenBalance daiBalance = TokenBalanceMother.daiResult();

        when(web3JGateway.getTokenBalances("address", Arrays.asList(fnd.getAddress(), zrx.getAddress(), dai.getAddress()))).thenReturn(
                Arrays.asList(new BigInteger(fndBalance.getRawBalance()), new BigInteger(zrxBalance.getRawBalance()), new BigInteger(daiBalance.getRawBalance())));
        when(tokenDiscoveryService.getTokens(SecretType.GOCHAIN)).thenReturn(Arrays.asList(fnd, zrx, dai));
        final List<TokenBalance> results = balanceGateway.getTokenBalances("address");
        assertThat(results).containsExactlyInAnyOrder(fndBalance, zrxBalance, daiBalance);
    }
}
