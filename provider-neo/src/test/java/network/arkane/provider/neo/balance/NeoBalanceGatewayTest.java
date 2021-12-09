package network.arkane.provider.neo.balance;

import io.neow3j.model.types.GASAsset;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.balance.domain.TokenBalanceMother;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
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

class NeoBalanceGatewayTest {
    private NeoBalanceGateway balanceGateway;
    private NeoW3JGateway neoGateway;
    private TokenDiscoveryService tokenDiscoveryService;

    @BeforeEach
    void setUp() {
        neoGateway = mock(NeoW3JGateway.class);
        NeoGetAccountState.Balance balance = mock(NeoGetAccountState.Balance.class);
        when(neoGateway.getBalance(any(String.class))).thenReturn(
                Arrays.asList(new NeoGetAccountState.Balance("0x" + NEOAsset.HASH_ID, "1"),
                        new NeoGetAccountState.Balance("0x" + GASAsset.HASH_ID, "2.2")));
        when(neoGateway.getTokenBalance(any(String.class), any(String.class))).thenReturn(BigInteger.valueOf(1000000000000000000L));
        tokenDiscoveryService = mock(TokenDiscoveryService.class);

        balanceGateway = new NeoBalanceGateway(neoGateway, tokenDiscoveryService);
    }

    @Test
    void checkBalance() {
        Balance balance = balanceGateway.getBalance("address");
        assertThat(balance.getBalance()).isEqualTo(1);
        assertThat(balance.getRawBalance()).isEqualTo("1");
        assertThat(balance.getGasBalance()).isEqualTo(2.2);
        assertThat(balance.getRawGasBalance()).isEqualTo("220000000");
        assertThat(balance.getDecimals()).isEqualTo(8);
    }

    @Test
    void checkTokenBalance() {
        final TokenInfo fndTokenInfo = TokenInfoMother.fnd().build();
        final TokenBalance fndBalance = TokenBalanceMother.fndResult();

        when(tokenDiscoveryService.getTokenInfo(SecretType.NEO, fndTokenInfo.getAddress())).thenReturn(Optional.of(fndTokenInfo));
        when(neoGateway.getTokenBalance("address", fndTokenInfo.getAddress())).thenReturn(new BigInteger(fndBalance.getRawBalance()));

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

        when(neoGateway.getTokenBalances("address", Arrays.asList(fnd.getAddress(), zrx.getAddress(), dai.getAddress()))).thenReturn(
                Arrays.asList(new BigInteger(fndBalance.getRawBalance()), new BigInteger(zrxBalance.getRawBalance()), new BigInteger(daiBalance.getRawBalance())));
        when(tokenDiscoveryService.getTokens(SecretType.NEO)).thenReturn(Arrays.asList(fnd, zrx, dai));
        final List<TokenBalance> results = balanceGateway.getTokenBalances("address");
        assertThat(results).containsExactlyInAnyOrder(fndBalance, zrxBalance, daiBalance);

    }
}
