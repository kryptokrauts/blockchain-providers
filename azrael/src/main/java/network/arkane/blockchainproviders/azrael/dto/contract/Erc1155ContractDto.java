package network.arkane.blockchainproviders.azrael.dto.contract;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.blockchainproviders.azrael.dto.ContractType;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Erc1155ContractDto extends ContractDto {

    @Builder
    public Erc1155ContractDto(String address,
                              ContractType contractType,
                              String name,
                              String symbol) {
        super(address, contractType, name, symbol);
    }
}
