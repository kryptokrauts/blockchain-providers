package network.arkane.provider.balance;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.Prefix;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.balance.domain.TokenBalanceMother;
import network.arkane.provider.core.model.blockchain.Account;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.ERC20Token;
import network.arkane.provider.gateway.VechainGateway;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import network.arkane.provider.token.TokenInfoMother;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static network.arkane.provider.chain.SecretType.VECHAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VechainBalanceGatewayTest {

    private VechainBalanceGateway balanceGateway;
    private VechainGateway vechainGateway;
    private TokenDiscoveryService tokenDiscoveryService;

    @BeforeEach
    void setUp() {
        vechainGateway = mock(VechainGateway.class);
        tokenDiscoveryService = mock(TokenDiscoveryService.class);
        balanceGateway = new VechainBalanceGateway(vechainGateway, tokenDiscoveryService);
    }

    @Test
    void getBalance() {
        final Account account = new Account();
        account.setBalance(BytesUtils.toHexString(BigInteger.valueOf(1000000000000000000L).toByteArray(), Prefix.ZeroLowerX));
        account.setEnergy(BytesUtils.toHexString(BigInteger.valueOf(2000000000000000000L).toByteArray(), Prefix.ZeroLowerX));

        when(vechainGateway.getAccount("anyAccount")).thenReturn(account);

        final Balance result = balanceGateway.getBalance("anyAccount");

        assertThat(result.getBalance()).isEqualTo(1);
        assertThat(result.getGasBalance()).isEqualTo(2);
        assertThat(result.getDecimals()).isEqualTo(18);
        assertThat(result.getRawBalance()).isEqualTo("1000000000000000000");
        assertThat(result.getRawGasBalance()).isEqualTo("2000000000000000000");
    }

    @Test
    void checkTokenBalance() {
        final String walletAddress = "address";
        final TokenBalance expectedResult = TokenBalanceMother.vthoResult();
        final TokenInfo tokenInfo = TokenInfoMother.vtho().build();
        setUpToken(walletAddress, tokenInfo, expectedResult);

        when(tokenDiscoveryService.getTokenInfo(VECHAIN, tokenInfo.getAddress())).thenReturn(Optional.of(tokenInfo));

        final List<TokenBalance> result = balanceGateway.getTokenBalances(walletAddress, Arrays.asList(tokenInfo.getAddress()));

        assertThat(result).containsOnly(expectedResult);
    }

    @Test
    void getTokenBalances() {
        final String walletAddress = "address";
        final TokenInfo vthoTokenInfo = TokenInfoMother.vtho().build();
        final TokenInfo shaTokenInfo = TokenInfoMother.sha().build();
        final TokenBalance vthoResult = TokenBalanceMother.vthoResult();
        final TokenBalance shaResult = TokenBalanceMother.shaResult();
        setUpToken(walletAddress, vthoTokenInfo, vthoResult);
        setUpToken(walletAddress, shaTokenInfo, shaResult);

        List<TokenInfo> tokens = Arrays.asList(vthoTokenInfo, shaTokenInfo);
        when(tokenDiscoveryService.getTokens(VECHAIN)).thenReturn(tokens);
        when(vechainGateway.getTokenBalances(walletAddress, tokens.stream().map(TokenInfo::getAddress).collect(Collectors.toList())))
                .thenReturn(Arrays.asList(new BigInteger(vthoResult.getRawBalance()), new BigInteger(shaResult.getRawBalance())));

        final List<TokenBalance> result = balanceGateway.getTokenBalances(walletAddress);

        AssertionsForInterfaceTypes.assertThat(result).contains(vthoResult, shaResult);
    }

    private void setUpToken(final String walletAddress, final TokenInfo tokenInfo, final TokenBalance result) {
        final ERC20Token token = ERC20Token.create(tokenInfo.getName(), Address.fromHexString(tokenInfo.getAddress()));
        token.setPrecision(new BigDecimal(tokenInfo.getDecimals()));
        final Amount amount = Amount.createFromToken(token).setBigIntegerAmount(new BigInteger(result.getRawBalance()));
        when(vechainGateway.getTokenBalance(same(walletAddress), refEq(token))).thenReturn(amount);
    }
}
