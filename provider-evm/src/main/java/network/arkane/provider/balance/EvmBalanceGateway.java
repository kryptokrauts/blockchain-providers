package network.arkane.provider.balance;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;

import java.util.List;

@Slf4j
public abstract class EvmBalanceGateway extends BalanceGateway {

    private final EvmBalanceStrategy evmBalanceStrategy;

    public EvmBalanceGateway(final List<EvmBalanceStrategy> balanceStrategies) {
        this.evmBalanceStrategy = balanceStrategies.stream().filter(b -> b.type() == type()).findFirst().orElseThrow();
    }

    public abstract SecretType type();

    @Override
    public Balance getBalance(final String account) {
        try {
            return evmBalanceStrategy.getBalance(account);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .message(String.format("Unable to get the balance for the specified account (%s) on chain (%s)", account, type()))
                                 .errorCode("web3.internal-error")
                                 .build();
        }
    }

    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress,
                                               final List<String> tokenAddresses) {
        return evmBalanceStrategy.getTokenBalances(walletAddress, tokenAddresses);
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return evmBalanceStrategy.getTokenBalances(walletAddress);
    }


}
