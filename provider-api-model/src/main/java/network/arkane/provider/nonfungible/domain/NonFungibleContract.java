package network.arkane.provider.nonfungible.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class NonFungibleContract {

    private String name;

    private String description;

    private String address;

    private String symbol;

    @JsonProperty("url")
    private String url;

    @JsonProperty("imageUrl")
    private String imageUrl;

    private JsonNode media;

    private String type;

    private boolean verified;

    private boolean premium;

    private boolean scam;

    private List<String> categories;

    @JsonSetter("externalUrl")
    private void setExternalUrl(String externalUrl) {
        if (this.url == null) {
            this.url = externalUrl;
        }
    }

    @JsonSetter("external_url")
    private void setExternal_url(String externalUrl) {
        if (this.url == null) {
            this.url = externalUrl;
        }
    }

    @JsonSetter("image")
    private void setImage(final String image) {
        if (this.imageUrl == null) {
            this.imageUrl = image;
        }
    }
}
