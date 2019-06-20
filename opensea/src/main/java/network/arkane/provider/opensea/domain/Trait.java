package network.arkane.provider.opensea.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
public class Trait {

    private String traitType;
    private String value;
    private String displayType;
    private BigInteger traitCount;

    @Builder
    public Trait(@JsonProperty("trait-type") final String traitType,
                 @JsonProperty("value") final String value,
                 @JsonProperty("display-type") final String displayType,
                 @JsonProperty("trait-count") final BigInteger traitCount) {
        this.traitType = traitType;
        this.value = value;
        this.displayType = displayType;
        this.traitCount = traitCount;
    }
}
