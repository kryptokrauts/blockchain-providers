package network.arkane.provider.bitcoin.sign;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.bitcoin.unspent.Unspent;
import network.arkane.provider.bitcoin.unspent.UnspentService;
import network.arkane.provider.exceptions.ArkaneException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class BitcoinTransactionFactory {

    private final NetworkParameters networkParameters;
    private final UnspentService unspentService;

    public BitcoinTransactionFactory(final BitcoinEnv bitcoinEnv, final UnspentService unspentService) {
        this.networkParameters = bitcoinEnv.getNetworkParameters();
        this.unspentService = unspentService;
    }

    public Transaction createBitcoinTransaction(final BitcoinTransactionSignable signable, final String from) {
        try {
            final Address fromAddress = Address.fromBase58(networkParameters, from);
            final Address toAddress = Address.fromBase58(networkParameters, signable.getAddress());
            final long amountToSend = signable.getSatoshiValue().longValue();

            final Transaction tx = new Transaction(networkParameters);
            tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
            tx.addOutput(Coin.valueOf(amountToSend), toAddress);
            addInputsAndOutputsToTransaction(fromAddress, tx, amountToSend, signable.getFeePerByte());
            tx.verify();
            return tx;
        } catch (final ArkaneException ex) {
            log.error(ex.getMessage());
            throw ex;
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("bitcoin.creation-error")
                                 .message(String.format("An error occurred trying to create the Bitcoin transaction: %s", ex.getMessage()))
                                 .build();
        }
    }

    private List<Unspent> fetchUnspents(final Address address) {
        final List<Unspent> unspentForAddress = unspentService.getUnspentForAddress(address);
        if (unspentForAddress.isEmpty()) {
            throw ArkaneException.arkaneException()
                                 .errorCode("bitcoin.transaction-inputs")
                                 .message("The account you're trying to use as origin in the transaction doesn't have valid inputs to send")
                                 .build();
        }
        return unspentForAddress;
    }

    @SneakyThrows
    private void addInputsAndOutputsToTransaction(final Address sourceAddress, final Transaction tx, final Long requiredAmount, int feePerByte) {

        final Iterator<Unspent> unspentIterator = fetchUnspents(sourceAddress).iterator();
        boolean isRequiredAmountCovered = false;
        long gatheredAmount = 0L;

        while (unspentIterator.hasNext() && !isRequiredAmountCovered) {
            final Unspent unspent = unspentIterator.next();

            gatheredAmount += unspent.getAmount();
            tx.addInput(createTransactionInput(tx, unspent));

            if (gatheredAmount >= requiredAmount) {
                isRequiredAmountCovered = addChangeAndTxFee(sourceAddress, tx, requiredAmount, gatheredAmount, feePerByte);
            }
        }

        if (gatheredAmount < requiredAmount) {
            throw ArkaneException.arkaneException()
                                 .errorCode("bitcoin.not-enough-funds")
                                 .message("Not enough funds to create the transaction")
                                 .build();
        }
    }

    private boolean addChangeAndTxFee(final Address sourceAddress, final Transaction tx, final long requiredAmount, final long gatheredAmount, final int feePerByte) {
        final long txFee = calculateTxFee(tx, feePerByte);
        if (gatheredAmount >= (requiredAmount + txFee)) {
            final Coin change = Coin.valueOf((gatheredAmount - requiredAmount - txFee));
            if (change.value > 0) {
                tx.addOutput(change, sourceAddress);
            }
            return true;
        }
        return false;
    }

    private long calculateTxFee(final Transaction tx, final int feePerByte) {
        return (tx.getInputs().size() * 148 + (tx.getOutputs().size() + 1) * 34 + 10) * feePerByte;
    }

    private TransactionInput createTransactionInput(final Transaction tx, final Unspent unspent) throws DecoderException {
        final TransactionOutPoint outPoint = new TransactionOutPoint(networkParameters, unspent.getVOut(), Sha256Hash.wrap(unspent.getTxId()));
        return new TransactionInput(networkParameters, tx, Hex.decodeHex(unspent.getScriptPubKey()), outPoint, Coin.valueOf(unspent.getAmount()));
    }
}
