package network.arkane.provider.balance.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenBalance {
    private String tokenAddress;
    private String rawBalance;
    private double balance;
    private int decimals;
    private String symbol;
    private String logo;
    private boolean available;

    @Builder
    public TokenBalance(String tokenAddress,
                        final String rawBalance,
                        final double balance,
                        final int decimals,
                        final String symbol,
                        final String logo) {
        this.tokenAddress = tokenAddress;
        this.rawBalance = rawBalance;
        this.balance = balance;
        this.decimals = decimals;
        this.symbol = symbol;
        this.logo = logo;
    }
}
