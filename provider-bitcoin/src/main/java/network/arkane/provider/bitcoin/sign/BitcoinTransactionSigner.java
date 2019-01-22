package network.arkane.provider.bitcoin.sign;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.secret.generation.BitcoinSecretKey;
import network.arkane.provider.bitcoin.wallet.generation.BitcoinKeystore;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.EncryptedData;
import org.bitcoinj.crypto.KeyCrypterScrypt;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.Protos;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@Slf4j
public class BitcoinTransactionSigner implements Signer<BitcoinTransactionSignable, BitcoinSecretKey> {

    private final NetworkParameters networkParameters;
    private final BitcoinTransactionFactory transactionFactory;

    public BitcoinTransactionSigner(final BitcoinEnv bitcoinEnv, final BitcoinTransactionFactory transactionFactory) {
        this.networkParameters = bitcoinEnv.getNetworkParameters();
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Signature createSignature(final BitcoinTransactionSignable signable, final BitcoinSecretKey secretKey) {
        try {
            final Address fromAddress = new Address(networkParameters, secretKey.getKey().getPubKeyHash());
            final Transaction tx = transactionFactory.createBitcoinTransaction(signable, fromAddress.toBase58());
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
                                 .errorCode("bitcoin.signing-error")
                                 .message(String.format("An error occurred trying to sign the bitcoin transaction: %s", ex.getMessage()))
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

    @Override
    public BitcoinSecretKey reconstructKey(final String secret, final String password) {
        BitcoinKeystore ed = JSONUtil.fromJson(secret, BitcoinKeystore.class);
        Protos.ScryptParameters params = Protos.ScryptParameters.newBuilder().setSalt(ByteString.copyFrom("".getBytes())).build();
        KeyCrypterScrypt crypter = new KeyCrypterScrypt(params);
        EncryptedData encryptedData = new EncryptedData(Base64.decodeBase64(ed.getInitialisationVector()), Base64.decodeBase64(ed.getEncryptedBytes()));
        return new BitcoinSecretKey(ECKey.fromEncrypted(encryptedData, crypter, Base64.decodeBase64(ed.getPubKey())));
    }
}
