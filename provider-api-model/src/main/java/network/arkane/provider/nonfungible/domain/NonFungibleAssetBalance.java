package network.arkane.provider.nonfungible.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NonFungibleAssetBalance {

    private NonFungibleAsset nonFungibleAsset;
    private BigInteger balance;

    @Builder(toBuilder = true)
    public NonFungibleAssetBalance(NonFungibleAsset nonFungibleAsset,
                                   BigInteger balance) {
        this.nonFungibleAsset = nonFungibleAsset;
        this.balance = balance == null ? BigInteger.ONE : balance;
    }
}
