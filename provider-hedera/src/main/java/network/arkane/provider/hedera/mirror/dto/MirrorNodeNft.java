package network.arkane.provider.hedera.mirror.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class MirrorNodeNft {
    @JsonAlias("account_id")
    private String accountId;

    @JsonAlias("created_timestamp")
    private String createdTimestamp;

    private String deleted;

    private String metadata;

    @JsonAlias("modified_timestamp")
    private String modifiedTimestamp;

    @JsonAlias("serial_number")
    private Long serialNumber;

    @JsonAlias("token_id")
    private String tokenId;
}
