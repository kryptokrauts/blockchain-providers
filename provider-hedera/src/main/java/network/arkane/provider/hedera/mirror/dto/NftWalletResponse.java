package network.arkane.provider.hedera.mirror.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NftWalletResponse {

    private List<NftWalletDto> nfts;
}
