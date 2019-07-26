package network.arkane.provider.neo.sign;

import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.crypto.transaction.RawTransactionInput;
import io.neow3j.crypto.transaction.RawTransactionOutput;
import io.neow3j.model.types.TransactionType;
import io.neow3j.transaction.InvocationTransaction;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigDecimal;
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

    private byte[] contractScript;
    private BigDecimal systemFee;

    @Builder
    public NeoTransactionSignable(final TransactionType transactionType,
                                  final List<RawTransactionAttribute> attributes,
                                  final List<RawTransactionInput> inputs,
                                  final List<RawTransactionOutput> outputs,
                                  final List<RawTransactionInput> claims,
                                  final  byte[] contractScript,
                                  final  BigDecimal systemFee ) {
        this.transactionType = transactionType;
        this.attributes = attributes == null ? new ArrayList<>() : attributes;
        this.inputs = inputs == null ? new ArrayList<>() : inputs;
        this.outputs = outputs == null ? new ArrayList<>() : outputs;
        this.claims = claims == null ? new ArrayList<>() : claims;
        this.contractScript = contractScript == null ? new byte[0] : contractScript;
        this.systemFee = systemFee == null ? BigDecimal.ZERO : systemFee;
    }
}