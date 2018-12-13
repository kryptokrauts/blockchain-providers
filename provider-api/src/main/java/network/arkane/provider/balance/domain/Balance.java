package network.arkane.provider.balance.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;

@Data
@NoArgsConstructor
public class Balance {

    private SecretType secretType;
    private double balance;
    private double gasBalance;
    private String symbol;
    private String gasSymbol;
    @Builder.Default
    private String rawBalance = "0";
    @Builder.Default
    private String rawGasBalance = "0";
    private int decimals;

    @Builder
    public Balance(final SecretType secretType,
                   final double balance,
                   final double gasBalance,
                   final String rawBalance,
                   final String rawGasBalance,
                   final String symbol,
                   final String gasSymbol,
                   final int decimals) {
        this.secretType = secretType;
        this.balance = balance;
        this.gasBalance = gasBalance;
        this.rawBalance = rawBalance;
        this.rawGasBalance = rawGasBalance;
        this.symbol = symbol;
        this.gasSymbol = gasSymbol;
        this.decimals = decimals;
    }
}
