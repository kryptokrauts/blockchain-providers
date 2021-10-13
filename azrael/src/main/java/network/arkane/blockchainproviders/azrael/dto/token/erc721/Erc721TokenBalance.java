package network.arkane.blockchainproviders.azrael.dto.token.erc721;

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
public class Erc721TokenBalance {
    private BigInteger tokenId;
    private BigInteger balance;
    private BigInteger finalBalance;
    private String tokenURI;
    private String metadata;
}
