package network.arkane.provider.nonfungible.domain;

import lombok.Builder;
import lombok.Data;


@Data
public class NonFungibleContract {

    private String name;
    private String description;
    private String address;
    private String symbol;
    private String url;
    private String imageUrl;
    private String type;

    @Builder
    public NonFungibleContract(final String name,
                               final String description,
                               final String address,
                               final String symbol,
                               final String url,
                               final String imageUrl,
                               final String type) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.symbol = symbol;
        this.url = url;
        this.imageUrl = imageUrl;
        this.type = type;
    }
}
