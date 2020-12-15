package network.arkane.blockchainproviders.azrael.dto.contract;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.blockchainproviders.azrael.dto.ContractType;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Erc20ContractDto extends ContractDto {
    private Integer decimals;

    @Builder
    public Erc20ContractDto(String address,
                            ContractType contractType,
                            String name,
                            String symbol,
                            Integer decimals) {
        super(address, contractType, name, symbol);
        this.decimals = decimals;
    }
}
