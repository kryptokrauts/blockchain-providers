package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockcypherTransactionRef {
    final String transactionHash;
    final BigInteger value;
    final BigInteger refBalance;
    final boolean spent;
    final long confirmations;
    final String transactionOutputN;
    final String transactionInputN;

    @Builder
    public BlockcypherTransactionRef(@JsonProperty("tx_hash") String transactionHash,
                                     @JsonProperty("value") BigInteger value,
                                     @JsonProperty("ref_balance") BigInteger refBalance,
                                     @JsonProperty("spent") boolean spent,
                                     @JsonProperty("confirmations") long confirmations,
                                     @JsonProperty("tx_output_n") String transactionOutputN,
                                     @JsonProperty("tx_input_n") String transactionInputN) {
        this.transactionHash = transactionHash;
        this.value = value;
        this.refBalance = refBalance;
        this.spent = spent;
        this.confirmations = confirmations;
        this.transactionOutputN = transactionOutputN;
        this.transactionInputN = transactionInputN;
    }
}
