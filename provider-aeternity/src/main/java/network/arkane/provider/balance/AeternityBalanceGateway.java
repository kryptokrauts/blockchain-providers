package network.arkane.provider.balance;

import com.kryptokrauts.aeternity.generated.model.Account;
import com.kryptokrauts.aeternity.sdk.service.account.AccountService;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;

import java.math.BigInteger;
import java.util.List;

public class AeternityBalanceGateway implements BalanceGateway {

    private AccountService accountService;

    public AeternityBalanceGateway(final AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Balance getBalance(String address) {
        try {
            final Account account = accountService.getAccount(address).blockingGet();
            final BigInteger balance = account.getBalance();
            return Balance.builder()
                    .secretType(SecretType.AETERNITY)
                    .decimals(18)
                    .symbol("AE")
                    .gasSymbol("AE")
                    .rawBalance(balance.toString())
                    .rawGasBalance(balance.toString())
                    .balance(PrecisionUtil.toDecimal(balance, 18))
                    .gasBalance(PrecisionUtil.toDecimal(balance, 18))
                    .build();
        } catch (final Exception e) {
            throw ArkaneException.arkaneException()
                    .message(String.format("Unable to get the balance for the specified address (%s)", address))
                    .errorCode("aeternity.internal-error")
                    .build();
        }
    }

    @Override
    public TokenBalance getTokenBalance(String address, String tokenAddress) {
        throw new UnsupportedOperationException("Not implemented yet for aeternity");
    }

    @Override
    public List<TokenBalance> getTokenBalances(String address) {
        throw new UnsupportedOperationException("Not implemented yet for aeternity");
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }
}
