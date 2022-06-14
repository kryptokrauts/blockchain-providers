package network.arkane.provider.nonfungible.domain;

import java.util.Collections;

import static network.arkane.provider.nonfungible.domain.NonFungibleContractMother.aCryptoAssaultNonFungibleContract;
import static network.arkane.provider.nonfungible.domain.NonFungibleContractMother.aGodsUnchainedNonFungibleContract;

public class NonFungibleAssetMother {

    public static NonFungibleAsset.NonFungibleAssetBuilder aGodsUnchainedNonFungibleBuilder() {
        return NonFungibleAsset.builder()
                               .id("7664")
                               .name("Pixielock")
                               .description("At the end of your turn, summon a 1/1 Nimble Pixie.")
                               .url("https://godsunchained.com/card/359127")
                               .backgroundColor("f3f3f3")
                               .imageUrl("https://images.godsunchained.com/cards/250/143.png")
                               .imagePreviewUrl("https://images.godsunchained.com/cards/250/143.png")
                               .imageThumbnailUrl("https://images.godsunchained.com/cards/250/143.png")
                               .attributes(Collections.emptyList())
                               .contract(aGodsUnchainedNonFungibleContract());
    }


    public static NonFungibleAsset aGodsUnchainedNonFungible() {
        return aGodsUnchainedNonFungibleBuilder().build();
    }

    public static NonFungibleAsset.NonFungibleAssetBuilder aCryptoAssaultNonFungibleBuilder() {
        return NonFungibleAsset.builder()
                               .id("4420")
                               .name("Common Predator MQ-28 #4420")
                               .backgroundColor("FBFBFB")
                               .description(
                                       "Strong against helis & tanks\\n\\nWeak against mechs & jeeps\\n\\nFlies over water, mountains, and ground units\\n\\nLow mining rate\\n")
                               .imageUrl("https://cryptoassault.io/units/unit_21.png")
                               .imagePreviewUrl("https://cryptoassault.io/units/unit_21.png")
                               .imageThumbnailUrl("https://cryptoassault.io/units/unit_21.png")
                               .animationUrl("https://cryptoassault.io/units/unit_21.mp4")
                               .url("https://cryptoassault.io/unit/?id=4420")
                               .attributes(Collections.emptyList())
                               .contract(aCryptoAssaultNonFungibleContract());
    }

    public static NonFungibleAsset aCryptoAssaultNonFungible() {
        return aCryptoAssaultNonFungibleBuilder().build();
    }
}
