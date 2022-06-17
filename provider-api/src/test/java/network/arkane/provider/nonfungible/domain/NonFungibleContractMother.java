package network.arkane.provider.nonfungible.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NonFungibleContractMother {

    public static NonFungibleContract aGodsUnchainedNonFungibleContract() {
        return NonFungibleContract.builder()
                                  .name("Gods Unchained")
                                  .description("Gods Unchained is a free-to-play, turn-based competitive trading card game. The goal of the game is to reduce your "
                                               + "opponent\'s life to zero. Players use their collection to build decks of cards, and select a God to play with at the "
                                               + "start of each match. Decks contain exactly 30 cards.")
                                  .url("https://godsunchained.com/")
                                  .address("0x6ebeaf8e8e946f0716e6533a6f2cefc83f60e8ab")
                                  .imageUrl("https://storage.opensea.io/0x6ebeaf8e8e946f0716e6533a6f2cefc83f60e8ab-featured-1556588688.png")
                                  .symbol("GODS")
                                  .build();

    }

    public static NonFungibleContract aCryptoAssaultNonFungibleContract() {
        return NonFungibleContract.builder()
                                  .name("CryptoAssault")
                                  .description(
                                          "Dominate you enemies in this War MMO! Command your armies in a HUGE 3D world in real-time where strategy "
                                          + "earns you daily ETHEREUM REWARDS. Capture territory, mine resources, form alliances and battle others NOW!")
                                  .url("https://cryptoassault.io/")
                                  .address("0x31af195db332bc9203d758c74df5a5c5e597cdb7")
                                  .imageUrl("https://storage.opensea.io/0x31af195db332bc9203d758c74df5a5c5e597cdb7-featured-1556588685.jpg")
                                  .symbol("CAU")
                                  .build();
    }
}
