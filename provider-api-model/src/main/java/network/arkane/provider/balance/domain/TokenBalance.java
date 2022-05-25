package network.arkane.provider.balance.domain;

import lombok.Builder;
import lombok.Data;

@Data
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
    private String name;

    @Builder
    public TokenBalance(String tokenAddress,
                        final String rawBalance,
                        final Double balance,
                        final Integer decimals,
                        final String symbol,
                        final String logo,
                        final String type,
                        final boolean transferable,
                        final String name) {
        this.tokenAddress = tokenAddress;
        this.rawBalance = rawBalance;
        this.balance = balance;
        this.decimals = decimals;
        this.symbol = symbol;
        this.logo = logo;
        this.type = type;
        this.transferable = transferable;
        this.name = name;
    }
}
