package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BlockchainProviderBalanceGateway extends BalanceGateway {

    private static final String BALANCE_URL = "/api/wallets/{walletAddress}/balance";
    private static final String TOKEN_BALANCES_URL = "/api/wallets/{walletAddress}/token-balances";
    private static final String PARAM_TOKEN_ADDRESSES = "tokenAddresses[]";

    private final SecretType secretType;
    private final BlockchainProviderGatewayClient client;

    public BlockchainProviderBalanceGateway(final SecretType secretType, BlockchainProviderGatewayClient client) {
        this.secretType = secretType;
        this.client = client;
    }

    @Override
    public Balance getBalance(final String address) {
        return client.get(BALANCE_URL, Balance.class, address);
    }

    @Override
    public Balance getZeroBalance() {
        return Balance.builder()
                      .available(true)
                      .rawBalance("0")
                      .rawGasBalance("0")
                      .secretType(secretType)
                      .gasBalance(0.0)
                      .balance(0.0)
                      .symbol(secretType.getSymbol())
                      .gasSymbol(secretType.getGasSymbol())
                      .decimals(18)
                      .build();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(TOKEN_BALANCES_URL);
        if (tokenAddresses != null && !tokenAddresses.isEmpty()) {
            uriComponentsBuilder.queryParam(PARAM_TOKEN_ADDRESSES, tokenAddresses);
        }
        final String url = uriComponentsBuilder.buildAndExpand(walletAddress).toUriString();
        return Arrays.asList(client.get(url, TokenBalance[].class));
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return getTokenBalances(walletAddress, List.of());
    }

    @Override
    public SecretType type() {
        return secretType;
    }

}
