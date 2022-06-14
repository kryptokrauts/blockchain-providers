package network.arkane.provider.balance;

import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.client.BlockchainProviderGatewayClient;

import java.util.List;

public class BlockchainProviderBalanceGateway extends BalanceGateway {

    private static final String BALANCE_URL = "/api/wallets/{walletAddress}/balance";

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
    public List<TokenBalance> getTokenBalances(final String address,
                                               final List<String> tokenAddresses) {
        return List.of();
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String address) {
        return List.of();
    }

    @Override
    public SecretType type() {
        return secretType;
    }

}
