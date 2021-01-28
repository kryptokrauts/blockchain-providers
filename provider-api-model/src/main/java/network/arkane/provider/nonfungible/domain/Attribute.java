package network.arkane.provider.nonfungible.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
public class Attribute {

    private String type;
    private String name;
    private String value;
    private String displayType;
    private BigInteger traitCount;
    private BigInteger maxValue;

    @Builder
    public Attribute(@JsonProperty("type") final String type,
                     @JsonProperty("name") @JsonAlias( {"trait_type", "traitType"}) final String name,
                     @JsonProperty("value") final String value,
                     @JsonProperty("displayType") @JsonAlias( {"display_type", "displayType"}) final String displayType,
                     @JsonProperty("traitCount") @JsonAlias( {"trait_count", "traitCount"}) final BigInteger traitCount,
                     @JsonProperty("maxValue") @JsonAlias( {"max_value", "maxValue"}) final BigInteger maxValue) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.displayType = displayType;
        this.traitCount = traitCount;
        this.maxValue = maxValue;
    }

}
