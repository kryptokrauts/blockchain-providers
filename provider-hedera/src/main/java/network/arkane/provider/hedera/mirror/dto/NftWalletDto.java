package network.arkane.provider.hedera.mirror.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NftWalletDto {

    @JsonAlias("account_id")
    private String accountId;
    @JsonAlias("created_timestamp")
    private String createdTimestamp;
    @JsonAlias("delegating_spender")
    private String delegatingSpender;
    private String metadata;
    @JsonAlias("serial_number")
    private int serialNumber;
    private String spender;
    @JsonAlias("token_id")
    private String tokenId;
}
