package network.arkane.blockchainproviders.azrael.dto.token.erc721;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.TokenBalance;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Erc721TokenBalances extends TokenBalance {
    private String name;
    private String symbol;
    private List<Erc721TokenBalance> tokens;

    @Builder
    public Erc721TokenBalances(ContractType type,
                               String address,
                               String name,
                               String symbol,
                               List<Erc721TokenBalance> tokens) {
        super(type, address);
        this.name = name;
        this.symbol = symbol;
        this.tokens = tokens;
    }
}
