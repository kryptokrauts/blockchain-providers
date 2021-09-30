package network.arkane.blockchainproviders.azrael.dto.token.erc20;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.TokenBalance;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Erc20TokenBalance extends TokenBalance {
    private String name;
    private String symbol;
    private Integer decimals;
    private BigInteger balance;
    private BigInteger finalBalance;

    @Builder
    public Erc20TokenBalance(ContractType type,
                             String address,
                             String name,
                             String symbol,
                             Integer decimals,
                             BigInteger balance,
                             BigInteger finalBalance) {
        super(type, address);
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.balance = balance;
        this.finalBalance = finalBalance;
    }
}
