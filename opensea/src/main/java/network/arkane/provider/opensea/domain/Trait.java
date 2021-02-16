package network.arkane.provider.opensea.domain;

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
    public Trait(@JsonProperty("trait_type") @JsonAlias("traitType") final String traitType,
                 @JsonProperty("value") final String value,
                 @JsonProperty("display_type") @JsonAlias("displayType") final String displayType,
                 @JsonProperty("traitCount") @JsonAlias("traitCount") final String traitCount,
                 @JsonProperty("max_value") @JsonAlias("maxValue") final String maxValue) {
        this.traitType = traitType;
        this.value = value;
        this.displayType = displayType;
        this.traitCount = parseToNumber(traitCount);
        this.maxValue = parseToNumber(maxValue);
    }

    private BigInteger parseToNumber(String value) {
        try {
            return new BigInteger(value);
        } catch (Exception e) {
            return null;
        }
    }
}
