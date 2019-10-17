package network.arkane.provider.aeternity.balance;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.kryptokrauts.aeternity.sdk.service.account.domain.AccountResult;
import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import io.vertx.core.json.Json;
import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class AeternityBalanceGateway extends BalanceGateway {

    private AeternityService aeternityService;

    public AeternityBalanceGateway(final @Qualifier("aeternity-service") AeternityService aeternityService) {
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.aeternityService = aeternityService;
    }

    @Override
    public Balance getBalance(String address) {
        try {
            final AccountResult account = aeternityService.accounts.asyncGetAccount(Optional.of(address)).timeout(5, TimeUnit.SECONDS).blockingGet();
            final BigInteger balance = account.getBalance();
            return Balance.builder()
                    .available(true)
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
            return Balance.builder()
                    .available(true)
                    .secretType(SecretType.AETERNITY)
                    .decimals(18)
                    .symbol("AE")
                    .gasSymbol("AE")
                    .rawBalance("0")
                    .rawGasBalance("0")
                    .balance(0)
                    .gasBalance(0)
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
