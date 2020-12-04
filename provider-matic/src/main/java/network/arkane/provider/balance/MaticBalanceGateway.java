package network.arkane.provider.balance;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MaticBalanceGateway extends BalanceGateway {

    private MaticBalanceStrategy maticBalanceStrategy;

    public MaticBalanceGateway(final MaticBalanceStrategy maticBalanceStrategy) {
        this.maticBalanceStrategy = maticBalanceStrategy;
    }

    @Override
    public SecretType type() {
        return SecretType.MATIC;
    }

    @Override
    @HystrixCommand(fallbackMethod = "unavailableBalance", commandKey = "matic-node")
    public Balance getBalance(final String account) {
        try {
            return maticBalanceStrategy.getBalance(account);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .message(String.format("Unable to get the balance for the specified account (%s)", account))
                                 .errorCode("web3.internal-error")
                                 .build();
        }
    }

    @Override
    public TokenBalance getTokenBalance(final String walletAddress,
                                        final String tokenAddress) {
        return maticBalanceStrategy.getTokenBalance(walletAddress, tokenAddress);
    }


    @Override
    public List<TokenBalance> getTokenBalances(final String walletAddress) {
        return maticBalanceStrategy.getTokenBalances(walletAddress);
    }


}
