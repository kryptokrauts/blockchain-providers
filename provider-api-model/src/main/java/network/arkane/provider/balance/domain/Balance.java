package network.arkane.provider.balance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {

    @Builder.Default
    private boolean available = true;
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

}
