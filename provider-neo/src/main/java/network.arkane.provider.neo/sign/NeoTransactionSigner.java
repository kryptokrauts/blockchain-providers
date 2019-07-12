package network.arkane.provider.neo.sign;

import io.neow3j.crypto.Keys;
import io.neow3j.crypto.Sign;
import io.neow3j.crypto.transaction.RawInvocationScript;
import io.neow3j.crypto.transaction.RawVerificationScript;
import io.neow3j.model.types.TransactionType;
import io.neow3j.protocol.transaction.ClaimTransaction;
import io.neow3j.protocol.transaction.ContractTransaction;
import io.neow3j.utils.Numeric;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;
import io.neow3j.crypto.transaction.RawTransaction;

import java.util.Arrays;
import java.util.List;


@Component
public class NeoTransactionSigner implements Signer<NeoTransactionSignable, NeoSecretKey> {

    @Override
    public Signature createSignature(final NeoTransactionSignable signable, final NeoSecretKey key) {
        final RawTransaction rawTx = constructTransaction(signable);

        // Unsigned message
        final RawTransaction unsignTx = constructTransaction(signable);

        // serialize the base raw transaction
        byte[] rawTxUnsignedArray = unsignTx.toArray();

        // Create the Invocation Script
        List<RawInvocationScript> rawInvocationScriptList = Arrays.asList(
                new RawInvocationScript(Sign.signMessage(rawTxUnsignedArray, key.getKey())));

        // Create the Verification Script
        byte[] publicKeyByteArray = Numeric.hexStringToByteArray(
                Numeric.toHexStringNoPrefix(key.getKey().getPublicKey()));
        RawVerificationScript verificationScript = Keys.getVerificationScriptFromPublicKey(publicKeyByteArray);

        rawTx.addScript(rawInvocationScriptList, verificationScript);

        final String prettify = Numeric.toHexStringNoPrefix(rawTx.toArray());
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(prettify)
                .build();
    }

    private RawTransaction constructTransaction(final NeoTransactionSignable signTransactionRequest) {
        if (signTransactionRequest.getTransactionType() == TransactionType.INVOCATION_TRANSACTION) {
            throw new NotImplementedException();
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