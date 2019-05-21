package network.arkane.provider.neo.balance;

import io.neow3j.model.types.GASAsset;
import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.neo.gateway.NeoW3JGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
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
                Arrays.asList(new NeoGetAccountState.Balance(NEOAsset.HASH_ID,"1.1"),
                        new NeoGetAccountState.Balance(GASAsset.HASH_ID,"2.2")));
        when(neoGateway.getTokenBalance(any(String.class), any(String.class))).thenReturn(BigInteger.valueOf(1000000000000000000L));
        tokenDiscoveryService = mock(TokenDiscoveryService.class);

        balanceGateway = new NeoBalanceGateway(neoGateway, tokenDiscoveryService);
    }

    @Test
    void checkBalance() {
        Balance balance = balanceGateway.getBalance("address");
        assertThat(balance.getBalance()).isEqualTo(1.1);
        assertThat(balance.getRawBalance()).isEqualTo("110000000");
        assertThat(balance.getGasBalance()).isEqualTo(2.2);
        assertThat(balance.getRawGasBalance()).isEqualTo("220000000");
        assertThat(balance.getDecimals()).isEqualTo(8);
    }

    @Test
    void checkTokenBalance() {
//        final TokenInfo fndTokenInfo = TokenInfoMother.fnd().build();
//        final TokenBalance fndBalance = TokenBalanceMother.fndResult();
//
//        when(tokenDiscoveryService.getTokenInfo(SecretType.NEO, fndTokenInfo.getAddress())).thenReturn(Optional.of(fndTokenInfo));
//        when(neoGateway.getTokenBalance("address", fndTokenInfo.getAddress())).thenReturn(new BigInteger(fndBalance.getRawBalance()));
//
//        final TokenBalance result = balanceGateway.getTokenBalance("address", fndTokenInfo.getAddress());
//
//        assertThat(result).isEqualTo(fndBalance);
    }

    @Test
    void getTokenBalances() {

    }
}