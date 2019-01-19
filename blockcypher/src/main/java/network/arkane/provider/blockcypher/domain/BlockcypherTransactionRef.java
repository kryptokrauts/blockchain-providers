package network.arkane.provider.blockcypher.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockcypherTransactionRef {
    final String transactionHash;
    final BigInteger value;
    final BigInteger refBalance;
    final boolean spent;
    final long confirmations;
    final BigInteger transactionOutputN;
    final BigInteger transactionInputN;
    final String script;

    @Builder
    public BlockcypherTransactionRef(@JsonProperty("tx_hash") String transactionHash,
                                     @JsonProperty("value") BigInteger value,
                                     @JsonProperty("ref_balance") BigInteger refBalance,
                                     @JsonProperty("spent") boolean spent,
                                     @JsonProperty("confirmations") long confirmations,
                                     @JsonProperty("tx_output_n") BigInteger transactionOutputN,
                                     @JsonProperty("tx_input_n") BigInteger transactionInputN,
                                     @JsonProperty("script") String script) {
        this.transactionHash = transactionHash;
        this.value = value;
        this.refBalance = refBalance;
        this.spent = spent;
        this.confirmations = confirmations;
        this.transactionOutputN = transactionOutputN;
        this.transactionInputN = transactionInputN;
        this.script = script;
    }
}
