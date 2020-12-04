package network.arkane.blockchainproviders.azrael.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.blockchainproviders.azrael.dto.token.erc1155.Erc1155TokenBalances;
import network.arkane.blockchainproviders.azrael.dto.token.erc20.Erc20TokenBalance;
import network.arkane.blockchainproviders.azrael.dto.token.erc721.Erc721TokenBalances;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes( {
                       @JsonSubTypes.Type(value = Erc20TokenBalance.class, name = "ERC_20"),
                       @JsonSubTypes.Type(value = Erc721TokenBalances.class, name = "ERC_721"),
                       @JsonSubTypes.Type(value = Erc1155TokenBalances.class, name = "ERC_1155"),
               })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TokenBalance {
    private ContractType type;
    private String address;

    public TokenBalance(ContractType type,
                        String address) {
        this.type = type;
        this.address = address;
    }
}
