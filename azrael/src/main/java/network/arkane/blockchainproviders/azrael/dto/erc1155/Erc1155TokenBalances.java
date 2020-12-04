package network.arkane.blockchainproviders.azrael.dto.erc1155;

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
public class Erc1155TokenBalances extends TokenBalance {
    private List<Erc1155TokenBalance> tokens;

    @Builder
    public Erc1155TokenBalances(ContractType type,
                                String address,
                                List<Erc1155TokenBalance> tokens) {
        super(type, address);
        this.tokens = tokens;
    }
}
