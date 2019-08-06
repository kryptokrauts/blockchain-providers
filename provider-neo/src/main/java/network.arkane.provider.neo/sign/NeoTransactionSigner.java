package network.arkane.provider.neo.sign;

import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.model.types.TransactionType;
import io.neow3j.transaction.ClaimTransaction;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Numeric;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;
import io.neow3j.crypto.transaction.RawTransaction;

@Component
public class NeoTransactionSigner implements Signer<NeoTransactionSignable, NeoSecretKey> {

    @Override
    public Signature createSignature(final NeoTransactionSignable signable, final NeoSecretKey key) {
        final RawTransaction rawTx = constructTransaction(signable);

        // Unsigned message
        final RawTransaction unsignTx = constructTransaction(signable);

        // serialize the base raw transaction
        byte[] rawTxUnsignedArray = unsignTx.toArrayWithoutScripts();

        rawTx.addScript(RawScript.createWitness(rawTxUnsignedArray, key.getKey()));

        final String prettify = Numeric.toHexStringNoPrefix(rawTx.toArray());
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(prettify)
                .build();
    }

    private RawTransaction constructTransaction(final NeoTransactionSignable signTransactionRequest) {
        if (signTransactionRequest.getTransactionType() == TransactionType.INVOCATION_TRANSACTION) {
            return new InvocationTransaction.Builder()
                    .inputs(signTransactionRequest.getInputs())
                    .outputs(signTransactionRequest.getOutputs())
                    .attributes(signTransactionRequest.getAttributes())
                    .contractScript(signTransactionRequest.getContractScript())
                    .systemFee(signTransactionRequest.getSystemFee())
                    .build();
        } else if (signTransactionRequest.getTransactionType() == TransactionType.CLAIM_TRANSACTION) {
            return new ClaimTransaction.Builder()
                    .outputs(signTransactionRequest.getOutputs())
                    .claims(signTransactionRequest.getClaims())
                    .attributes(signTransactionRequest.getAttributes())
                    .build();
        } else {
            return new ContractTransaction.Builder()
                    .inputs(signTransactionRequest.getInputs())
                    .outputs(signTransactionRequest.getOutputs())
                    .attributes(signTransactionRequest.getAttributes())
                    .build();

        }

    }

}