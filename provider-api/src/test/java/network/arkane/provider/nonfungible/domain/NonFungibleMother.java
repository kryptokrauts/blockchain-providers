package network.arkane.provider.nonfungible.domain;

public class NonFungibleMother {

    public static NonFungible.NonFungibleBuilder aGodsUnchainedNonFungibleBuilder() {
        return NonFungible.builder()
                          .id("7664")
                          .name("Pixielock")
                          .description("At the end of your turn, summon a 1/1 Nimble Pixie.")
                          .url("https://godsunchained.com/card/359127")
                          .backgroundColor("f3f3f3")
                          .imageUrl("https://images.godsunchained.com/cards/250/143.png")
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

    public static NonFungible aGodsUnchainedNonFungible() {
        return aGodsUnchainedNonFungibleBuilder().build();
    }
}
