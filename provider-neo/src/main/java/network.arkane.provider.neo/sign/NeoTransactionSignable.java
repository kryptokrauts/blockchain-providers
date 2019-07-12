package network.arkane.provider.neo.sign;

import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.crypto.transaction.RawTransactionInput;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NeoTransactionSignable implements Signable {

    private TransactionType transactionType;
    private List<RawTransactionAttribute> attributes;
    private List<RawTransactionInput> inputs;
    private List<RawTransactionOutput> outputs;

    private List<RawTransactionInput> claims;

    @Builder
    public NeoTransactionSignable(final TransactionType transactionType,
                                  final List<RawTransactionAttribute> attributes,
                                  final List<RawTransactionInput> inputs,
                                  final List<RawTransactionOutput> outputs,
                                  final List<RawTransactionInput> claims) {

        this.attributes = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();

        this.attributes = attributes == null ? new ArrayList<>() : attributes;
        this.inputs = inputs == null ? new ArrayList<>() : inputs;
        this.outputs = outputs == null ? new ArrayList<>() : outputs;
        this.claims = claims == null ? new ArrayList<>() : claims;
    }
}