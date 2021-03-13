package network.arkane.blockchainproviders.covalent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CovalentItem {

    @JsonProperty("contract_address")
    private String tokenAddress;

    @JsonProperty("contract_name")
    private String name;

    @JsonProperty("contract_ticker_symbol")
    private String symbol;

    @JsonProperty("contract_decimals")
    private Integer contractDecimals;

    @JsonProperty("balance")
    private BigInteger balance;

    @JsonProperty("logo_url")
    private String logo;
}
