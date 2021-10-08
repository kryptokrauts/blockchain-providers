package network.arkane.provider.nonfungible.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    @JsonProperty("displayType")
    private String displayType;

    @JsonProperty("traitCount")
    private BigInteger traitCount;

    @JsonProperty("maxValue")
    private BigInteger maxValue;

    @JsonSetter("trait_type")
    private void setTrait_type(final String traitType) {
        if (this.name == null) {
            this.name = traitType;
        }
    }

    @JsonSetter("traitType")
    private void setTraitType(final String traitType) {
        if (this.name == null) {
            this.name = traitType;
        }
    }

    @JsonSetter("display_type")
    private void setDisplay_type(final String displayType) {
        if (this.displayType == null) {
            this.displayType = displayType;
        }
    }

    @JsonSetter("trait_count")
    private void setTrait_count(final BigInteger traitCount) {
        if (this.traitCount == null) {
            this.traitCount = traitCount;
        }
    }

    @JsonSetter("max_value")
    private void setMax_value(final BigInteger maxValue) {
        if (this.maxValue == null) {
            this.maxValue = maxValue;
        }
    }

}
