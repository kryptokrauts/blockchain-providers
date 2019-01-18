package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockcypherAddress {
    final String address;
    final BigInteger totalReceived;
    final BigInteger totalSent;
    final BigInteger balance;
    final BigInteger unconfirmedBalance;
    final BigInteger finalBalance;
    final int nTx;
    final int unconfirmedNTx;
    final int finalNTx;

    @Builder
    public BlockcypherAddress(@JsonProperty("address") String address,
                              @JsonProperty("total_received") BigInteger totalReceived,
                              @JsonProperty("total_sent") BigInteger totalSent,
                              @JsonProperty("balance") BigInteger balance,
                              @JsonProperty("unconfirmed_balance") BigInteger unconfirmedBalance,
                              @JsonProperty("final_balance") BigInteger finalBalance,
                              @JsonProperty("n_tx") int nTx,
                              @JsonProperty("unconfirmed_n_tx") final int unconfirmedNTx,
                              @JsonProperty("final_n_tx") int finalNTx) {
        this.address = address;
        this.totalReceived = totalReceived;
        this.totalSent = totalSent;
        this.balance = balance;
        this.unconfirmedBalance = unconfirmedBalance;
        this.finalBalance = finalBalance;
        this.nTx = nTx;
        this.unconfirmedNTx = unconfirmedNTx;
        this.finalNTx = finalNTx;
    }
}
