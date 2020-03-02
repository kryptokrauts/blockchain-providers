package network.arkane.provider.nonfungible.domain;

public class NonFungibleAssetMother {

    public static NonFungibleAsset.NonFungibleAssetBuilder aGodsUnchainedNonFungibleBuilder() {
        return NonFungibleAsset.builder()
                               .tokenId("7664")
                               .name("Pixielock")
                               .description("At the end of your turn, summon a 1/1 Nimble Pixie.")
                               .url("https://godsunchained.com/card/359127")
                               .backgroundColor("f3f3f3")
                               .imageUrl("https://images.godsunchained.com/cards/250/143.png")
                               .imagePreviewUrl("https://images.godsunchained.com/cards/250/143.png")
                               .imageThumbnailUrl("https://images.godsunchained.com/cards/250/143.png")
                               .contract(NonFungibleContract.builder()
                                                       .name("Gods Unchained")
                                                       .description("Gods Unchained is a free-to-play, turn-based competitive trading"
                                                                    + "card game. The goal of the game is to reduce your "
                                                                    + "opponent\'s life to zero. Players use their collection to "
                                                                    + "build decks of cards, and select a God to play with at the "
                                                                    + "start of each match. Decks contain exactly 30 cards.")
                                                       .address("0x6ebeaf8e8e946f0716e6533a6f2cefc83f60e8ab")
                                                       .symbol("GODS")
                                                       .url("https://godsunchained.com/")
                                                       .imageUrl("https://storage.opensea.io/"
                                                                 + "0x6ebeaf8e8e946f0716e6533a6f2cefc83f60e8ab-featured-1556588688"
                                                                 + ".png")
                                                       .build());
    }


    public static NonFungibleAsset aGodsUnchainedNonFungible() {
        return aGodsUnchainedNonFungibleBuilder().build();
    }

    public static NonFungibleAsset.NonFungibleAssetBuilder aCryptoAssaultNonFungibleBuilder() {
        return NonFungibleAsset.builder()
                               .tokenId("4420")
                               .name("Common Predator MQ-28 #4420")
                               .backgroundColor("FBFBFB")
                               .description("Strong against helis & tanks\\n\\nWeak against mechs & jeeps\\n\\nFlies over water, mountains, and ground units\\n\\nLow mining rate\\n")
                               .imageUrl("https://cryptoassault.io/units/unit_21.png")
                               .imagePreviewUrl("https://cryptoassault.io/units/unit_21.png")
                               .imageThumbnailUrl("https://cryptoassault.io/units/unit_21.png")
                               .url("https://cryptoassault.io/unit/?id=4420")
                               .contract(NonFungibleContract.builder()
                                                            .address("0x31af195db332bc9203d758c74df5a5c5e597cdb7")
                                                            .description(
                                                                    "Dominate you enemies in this War MMO! Command your armies in a HUGE 3D world in real-time where strategy earns you "
                                                                    + "daily ETHEREUM REWARDS. Capture territory, mine resources, form alliances and battle others NOW!")
                                                            .imageUrl("https://storage.opensea.io/0x31af195db332bc9203d758c74df5a5c5e597cdb7-featured-1556588685.jpg")
                                                            .name("CryptoAssault")
                                                            .symbol("CAU")
                                                            .url("https://cryptoassault.io/")
                                                            .build());
    }

    public static NonFungibleAsset aCryptoAssaultNonFungible() {
        return aCryptoAssaultNonFungibleBuilder().build();
    }
}
