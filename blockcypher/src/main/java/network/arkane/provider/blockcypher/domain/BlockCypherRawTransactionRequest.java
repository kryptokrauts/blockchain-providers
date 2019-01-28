package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockCypherRawTransactionRequest {

    @JsonProperty("tx")
    private String signedTransactionAsHex;

    public BlockCypherRawTransactionRequest() {
    }

    public BlockCypherRawTransactionRequest(String signedTransactionAsHex) {
        this.signedTransactionAsHex = signedTransactionAsHex;
    }

    public String getSignedTransactionAsHex() {
        return signedTransactionAsHex;
    }
}
