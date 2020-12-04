package network.arkane.blockchainproviders.azrael.dto.contract;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.blockchainproviders.azrael.dto.ContractType;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "contractType", visible = true, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes( {
                       @JsonSubTypes.Type(value = Erc20ContractDto.class, name = "ERC_20"),
                       @JsonSubTypes.Type(value = Erc721ContractDto.class, name = "ERC_721"),
                       @JsonSubTypes.Type(value = Erc1155ContractDto.class, name = "ERC_1155"),
               })
public abstract class ContractDto {
    private String address;
    private ContractType contractType;
    private String name;
    private String symbol;
}
