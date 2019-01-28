package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockCypherRawTransactionResponse {

    @JsonProperty("hash")
    private String transactionId;

    public BlockCypherRawTransactionResponse() {
    }

    public BlockCypherRawTransactionResponse(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
