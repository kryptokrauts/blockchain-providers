package network.arkane.blockchainproviders.blockscout.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class ERC1155BlockscoutToken extends BlockscoutToken {
    private List<BlockscoutTokenBalance> tokens;
}
