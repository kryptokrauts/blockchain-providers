package network.arkane.provider.nonfungible.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    private BigInteger maxValue;

    @Builder
    public Trait(@JsonProperty("traitType") @JsonAlias("trait_type") final String traitType,
                 @JsonProperty("value") final String value,
                 @JsonProperty("displayType") @JsonAlias("display_type") final String displayType,
                 @JsonProperty("traitCount") @JsonAlias("trait_count") final BigInteger traitCount,
                 @JsonProperty("maxValue") @JsonAlias("max_value") final BigInteger maxValue) {
        this.traitType = traitType;
        this.value = value;
        this.displayType = displayType;
        this.traitCount = traitCount;
        this.maxValue = maxValue;
    }

}
