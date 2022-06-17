package network.arkane.provider.nonfungible.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

import static network.arkane.provider.nonfungible.domain.NonFungibleAssetMother.aCryptoAssaultNonFungible;
import static network.arkane.provider.nonfungible.domain.NonFungibleAssetMother.aGodsUnchainedNonFungible;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NonFungibleAssetBalanceMother {

    public static NonFungibleAssetBalance aGodsUnchainedNonFungibleAssetBalance() {
        return NonFungibleAssetBalance.from(
                aGodsUnchainedNonFungible(),
                BigInteger.ONE,
                null
                                           );
    }

    public static NonFungibleAssetBalance aCryptoAssaultNonFungibleAssetBalance() {
        return NonFungibleAssetBalance.from(
                aCryptoAssaultNonFungible(),
                BigInteger.ONE,
               null
                                           );
    }
}
