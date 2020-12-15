package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.EthereumWeb3JGateway;
import network.arkane.provider.token.GithubTokenDiscoveryService;
import network.arkane.provider.token.TokenDiscoveryService;
import network.arkane.provider.token.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
class EthereumBalanceGatewayIntegrationTest {
    private Web3j web3j;
    private EthereumWeb3JGateway ethereumWeb3JGateway;
    private EthereumBalanceGateway ethereumBalanceGateway;
    private TokenDiscoveryService tokenDiscoveryService;
    private GithubTokenDiscoveryService githubTokenDiscoveryService;
    private EthereumNativeBalanceStrategy ethereumNativeBalanceStrategy;

    @BeforeEach
    void setUp() throws InterruptedException {
        web3j = Web3j.build(new HttpService("https://kovan.arkane.network"));
        ethereumWeb3JGateway = new EthereumWeb3JGateway(web3j, "0x40a38911e470fC088bEEb1a9480c2d69C847BCeC");
        Thread.sleep(100);
        tokenDiscoveryService = mock(TokenDiscoveryService.class);
        githubTokenDiscoveryService = mock(GithubTokenDiscoveryService.class);
        ethereumNativeBalanceStrategy = new EthereumNativeBalanceStrategy(ethereumWeb3JGateway, tokenDiscoveryService);
        ethereumBalanceGateway = new EthereumBalanceGateway(Collections.singletonList(ethereumNativeBalanceStrategy));
    }

    @Test
    void getBalance() {
        String tokenAddress = "0x865a0a385b805cC4146c0F4061586a3da25e5C1f";
        when(tokenDiscoveryService.getTokenInfo(SecretType.ETHEREUM, tokenAddress)).thenReturn(Optional.of(TokenInfo.builder()
                                                                                                                    .address(tokenAddress)
                                                                                                                    .decimals(9)
                                                                                                                    .name("0xEnergy")
                                                                                                                    .symbol("0xE")
                                                                                                                    .transferable(true)
                                                                                                                    .type("ERC20")
                                                                                                                    .build()));

        TokenBalance result = ethereumBalanceGateway.getTokenBalance("0xd4245b14ea0a885dbea5aa678dd5aba62505e36c", tokenAddress);
        System.out.println(result);
    }

    @Test
    void getWeenus() {
        String tokenAddress = "0xaFF4481D10270F50f203E0763e2597776068CBc5";
        when(githubTokenDiscoveryService.getTokens()).thenReturn(new HashMap<>());

        TokenBalance result = ethereumBalanceGateway.getTokenBalance("0xd4245b14ea0a885dbea5aa678dd5aba62505e36c", tokenAddress);
        System.out.println(result);
    }
}
