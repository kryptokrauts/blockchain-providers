package network.arkane.blockchainproviders.blockscout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class BlockscoutTokenBalance {
    @JsonProperty("token_id")
    private BigInteger tokenId;
    private Boolean fungible;
    private BigInteger balance;

    public Long getBalanceAsLong() {
        return balance == null ? 0 : balance.longValue();
    }
}
