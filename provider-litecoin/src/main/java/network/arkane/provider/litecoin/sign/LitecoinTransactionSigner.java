package network.arkane.provider.litecoin.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.secret.generation.LitecoinSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@Slf4j
public class LitecoinTransactionSigner implements Signer<LitecoinTransactionSignable, LitecoinSecretKey> {

    private final LitecoinEnv litecoinEnv;
    private final LitecoinTransactionFactory transactionFactory;

    public LitecoinTransactionSigner(LitecoinEnv litecoinEnv, final LitecoinTransactionFactory transactionFactory) {
        this.litecoinEnv = litecoinEnv;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Signature createSignature(final LitecoinTransactionSignable signable, final LitecoinSecretKey secretKey) {
        try {
            final Address fromAddress = new Address(litecoinEnv.getNetworkParameters(), secretKey.getKey().getPubKeyHash());
            final Transaction tx = transactionFactory.createLitecoinTransaction(signable, fromAddress.toBase58());
            signInputsOfTransaction(fromAddress, tx, secretKey.getKey());
            return network.arkane.provider.sign.domain.TransactionSignature.signTransactionBuilder()
                    .signedTransaction(Hex.encodeHexString(tx.bitcoinSerialize()))
                    .build();
        } catch (final ArkaneException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            throw ArkaneException.arkaneException()
                    .errorCode("litecoin.signing-error")
                    .message(String.format("An error occurred trying to sign the litecoin transaction: %s", ex.getMessage()))
                    .build();
        }
    }

    private void signInputsOfTransaction(final Address sourceAddress, final Transaction tx, final ECKey key) {
        IntStream.range(0, tx.getInputs().size()).forEach(i -> {
            final Script scriptPubKey = ScriptBuilder.createOutputScript(sourceAddress);
            final Sha256Hash hash = tx.hashForSignature(i, scriptPubKey, Transaction.SigHash.ALL, true);
            final ECKey.ECDSASignature ecdsaSignature = key.sign(hash);
            final TransactionSignature txSignature = new TransactionSignature(ecdsaSignature, Transaction.SigHash.ALL, true);
            if (scriptPubKey.isSentToRawPubKey()) {
                tx.getInput(i).setScriptSig(ScriptBuilder.createInputScript(txSignature));
            } else {
                if (!scriptPubKey.isSentToAddress()) {
                    throw new ScriptException("Unable to sign this scriptPubKey: " + scriptPubKey);
                }
                tx.getInput(i).setScriptSig(ScriptBuilder.createInputScript(txSignature, key));
            }
        });
    }
}
