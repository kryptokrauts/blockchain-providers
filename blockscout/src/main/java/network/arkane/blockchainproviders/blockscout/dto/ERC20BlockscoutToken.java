package network.arkane.blockchainproviders.blockscout.dto;

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
public class ERC20BlockscoutToken extends BlockscoutToken {
    private Integer decimals;
    private String symbol;
    private String name;
    private BigInteger balance;
}
