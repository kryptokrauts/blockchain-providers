package network.arkane.blockchainproviders.azrael.dto.contract;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenDto {
    private ContractDto contract;
    private String uri;
    private String metadata;
}
