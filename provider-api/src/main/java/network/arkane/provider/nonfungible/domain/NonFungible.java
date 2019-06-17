package network.arkane.provider.nonfungible.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class NonFungible {

    private String id;
    private String name;
    private String description;
    private String url;
    private String backgroundColor;
    private String imageUrl;
    private NonFungibleContract contract;

    @Builder
    public NonFungible(final String id,
                       final String name,
                       final String description,
                       final String url,
                       final String backgroundColor,
                       final String imageUrl,
                       final NonFungibleContract contract) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.contract = contract;
    }
}
