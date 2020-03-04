package network.arkane.provider.balance.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenBalance {
    private String tokenAddress;
    private String rawBalance;
    private Double balance;
    private Integer decimals;
    private String symbol;
    private String logo;
    private String type;
    @Builder.Default
    private boolean transferable = true;

    @Builder
    public TokenBalance(String tokenAddress,
                        final String rawBalance,
                        final Double balance,
                        final Integer decimals,
                        final String symbol,
                        final String logo,
                        final String type,
                        final boolean transferable) {
        this.tokenAddress = tokenAddress;
        this.rawBalance = rawBalance;
        this.balance = balance;
        this.decimals = decimals;
        this.symbol = symbol;
        this.logo = logo;
        this.type = type;
        this.transferable = transferable;
    }
}
