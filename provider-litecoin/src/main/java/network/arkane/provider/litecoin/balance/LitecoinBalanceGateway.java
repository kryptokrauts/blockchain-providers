package network.arkane.provider.litecoin.balance;

import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.address.LitecoinP2SHConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LitecoinBalanceGateway implements BalanceGateway {

    private final LitecoinEnv litecoinEnv;
    private final BlockcypherGateway blockcypherGateway;
    private final LitecoinP2SHConverter litecoinP2SHConverter;

    public LitecoinBalanceGateway(LitecoinEnv litecoinEnv,
                                  BlockcypherGateway blockcypherGateway,
                                  LitecoinP2SHConverter litecoinP2SHConverter) {
        this.litecoinEnv = litecoinEnv;
        this.blockcypherGateway = blockcypherGateway;
        this.litecoinP2SHConverter = litecoinP2SHConverter;
    }

    @Override
    public Balance getBalance(String address) {
        BlockcypherAddress balance = blockcypherGateway.getBalance(
                litecoinEnv.getNetwork(),
                litecoinP2SHConverter.convert(address)
        );

        double balanceAsDouble = balance.getBalance() == null
                ? 0
                : PrecisionUtil.toDecimal(balance.getBalance(), 8);

        String rawBalance = balance.getBalance() == null
                ? "0"
                : balance.getBalance().toString();

        return Balance
                .builder()
                .balance(balanceAsDouble)
                .gasBalance(balanceAsDouble)
                .rawBalance(rawBalance)
                .rawGasBalance(rawBalance)
                .decimals(8)
                .secretType(SecretType.LITECOIN)
                .symbol("LTC")
                .gasSymbol("LTC")
                .build();
    }

    @Override
    public TokenBalance getTokenBalance(String address, String tokenAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TokenBalance> getTokenBalances(String address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }
}
