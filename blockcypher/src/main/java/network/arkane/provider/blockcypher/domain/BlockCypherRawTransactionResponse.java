package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockCypherRawTransactionResponse {

    @JsonProperty("tx")
    private TX tx;

    public BlockCypherRawTransactionResponse() {
    }

    public BlockCypherRawTransactionResponse(TX tx) {
        this.tx = tx;
    }

    public String getTransactionId() {
        return tx.getHash();
    }
}
