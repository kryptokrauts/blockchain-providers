package network.arkane.blockchainproviders.blockscout.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes( {
                       @JsonSubTypes.Type(value = ERC20BlockscoutToken.class, name = "ERC-20"),
                       @JsonSubTypes.Type(value = ERC721BlockscoutToken.class, name = "ERC-721"),
                       @JsonSubTypes.Type(value = ERC1155BlockscoutToken.class, name = "ERC-1155"),
               }
)
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public abstract class BlockscoutToken {
    private String type;
    private String contractAddress;
}
