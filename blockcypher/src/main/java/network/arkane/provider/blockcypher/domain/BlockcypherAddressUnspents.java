package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockcypherAddressUnspents extends BlockcypherAddress {

    @Builder
    public BlockcypherAddressUnspents(@JsonProperty("address") String address,
                                      @JsonProperty("total_received") BigInteger totalReceived,
                                      @JsonProperty("total_sent") BigInteger totalSent,
                                      @JsonProperty("balance") BigInteger balance,
                                      @JsonProperty("unconfirmed_balance") BigInteger unconfirmedBalance,
                                      @JsonProperty("final_balance") BigInteger finalBalance,
                                      @JsonProperty("n_tx") int nTx,
                                      @JsonProperty("unconfirmed_n_tx") final int unconfirmedNTx,
                                      @JsonProperty("final_n_tx") int finalNTx) {
        super(address, totalReceived, totalSent, balance, unconfirmedBalance, finalBalance, nTx, unconfirmedNTx, finalNTx);
    }
}
