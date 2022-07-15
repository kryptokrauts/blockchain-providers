package network.arkane.provider.balance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
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
    private String name;
}
