package network.arkane.provider.litecoin.balance;

import network.arkane.provider.PrecisionUtil;
import network.arkane.provider.balance.BalanceGateway;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LitecoinBalanceGateway implements BalanceGateway {

    private final BlockcypherGateway blockcypherGateway;

    public LitecoinBalanceGateway(BlockcypherGateway blockcypherGateway) {
        this.blockcypherGateway = blockcypherGateway;
    }

    @Override
    public Balance getBalance(String address) {
        BlockcypherAddress balance = blockcypherGateway.getBalance(Network.LITECOIN, address);

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
