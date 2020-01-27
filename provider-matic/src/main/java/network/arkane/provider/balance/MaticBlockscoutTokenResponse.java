package network.arkane.provider.balance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MaticBlockscoutTokenResponse extends MaticBlockscoutResponse<MaticBlockscoutTokenResponse> {
    private BigInteger balance;
    private String contractAddress;
    private Integer decimals;
    private String name;
    private String symbol;
    private String type;
}
