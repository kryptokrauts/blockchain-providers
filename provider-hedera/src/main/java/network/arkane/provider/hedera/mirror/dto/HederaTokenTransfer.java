package network.arkane.provider.hedera.mirror.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class HederaTokenTransfer {

    @JsonAlias("token_id")
    private String tokenId;
    private String account;
    private BigInteger amount;
}
