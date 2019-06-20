package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Assets {

    private List<Asset> assets;

    @Builder
    public Assets(@JsonProperty("assets") List<Asset> assets) {
        this.assets = assets;
    }
}
