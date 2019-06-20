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
import java.util.List;

@Data
@NoArgsConstructor
public class NeoTransactionSignable implements Signable {

    private List<RawTransactionAttribute> attributes;
    private List<RawTransactionInput> inputs;
    private List<RawTransactionOutput> outputs;
    private List<RawScript> scripts;

    @Builder
    public NeoTransactionSignable(final List<RawTransactionAttribute> attributes,
                                       final List<RawTransactionInput> inputs,
                                       final List<RawTransactionOutput> outputs,
                                       final List<RawScript> scripts) {
        this.attributes = attributes;
        this.inputs = inputs;
        this.outputs = outputs;
        this.scripts = scripts;
    }
}