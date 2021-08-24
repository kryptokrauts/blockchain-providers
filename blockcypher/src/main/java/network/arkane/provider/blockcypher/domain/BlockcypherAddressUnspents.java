package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockcypherAddressUnspents extends BlockcypherAddress {

    private List<BlockcypherTransactionRef> transactionRefs;

    public BlockcypherAddressUnspents(@JsonProperty("address") String address,
                                      @JsonProperty("total_received") BigInteger totalReceived,
                                      @JsonProperty("total_sent") BigInteger totalSent,
                                      @JsonProperty("balance") BigInteger balance,
                                      @JsonProperty("unconfirmed_balance") BigInteger unconfirmedBalance,
                                      @JsonProperty("final_balance") BigInteger finalBalance,
                                      @JsonProperty("n_tx") int nTx,
                                      @JsonProperty("unconfirmed_n_tx") final int unconfirmedNTx,
                                      @JsonProperty("final_n_tx") int finalNTx,
                                      @JsonProperty("txrefs") List<BlockcypherTransactionRef> transactionRefs) {
        super(address, totalReceived, totalSent, balance, unconfirmedBalance, finalBalance, nTx, unconfirmedNTx, finalNTx, Collections.emptyList());
        this.transactionRefs = transactionRefs;
    }
}
