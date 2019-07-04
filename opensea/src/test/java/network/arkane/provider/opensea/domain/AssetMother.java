package network.arkane.provider.opensea.domain;

public class AssetMother {

    public static Asset.AssetBuilder aCryptoAssaultAssetBuilder() {
        return Asset.builder()
                    .tokenId("4420")
                    .name("Common Predator MQ-28 #4420")
                    .backgroundColor("FBFBFB")
                    .description("Strong against helis & tanks\\n\\nWeak against mechs & jeeps\\n\\nFlies over water, mountains, and ground units\\n\\nLow mining rate\\n")
                    .imageUrl("https://cryptoassault.io/units/unit_21.png")
                    .imagePreviewUrl("https://cryptoassault.io/units/unit_21.png")
                    .imageThumbnailUrl("https://cryptoassault.io/units/unit_21.png")
                    .externalLink("https://cryptoassault.io/unit/?id=4420")
                    .owner(Account.builder().address("0x0239769a1adf4def9f07da824b80b9c4fcb59593").build())
                    .assetContract(AssetContract.builder()
                                                .address("0x31af195db332bc9203d758c74df5a5c5e597cdb7")
                                                .description(
                                                        "Dominate you enemies in this War MMO! Command your armies in a HUGE 3D world in real-time "
                                                        + "where strategy earns you "
                                                        + "daily ETHEREUM REWARDS. Capture territory, mine resources, form alliances and battle others "
                                                        + "NOW!")
                                                .imageUrl("https://storage.opensea.io/0x31af195db332bc9203d758c74df5a5c5e597cdb7-featured-1556588685.jpg")
                                                .name("CryptoAssault")
                                                .symbol("CAU")
                                                .externalLink("https://cryptoassault.io/")
                                                .build());
    }

    public static Asset aCryptoAssaultAsset() {
        return aCryptoAssaultAssetBuilder().build();
    }
}
