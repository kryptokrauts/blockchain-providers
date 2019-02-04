package network.arkane.provider.litecoin.sign;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import network.arkane.provider.litecoin.unspent.Unspent;
import network.arkane.provider.litecoin.unspent.UnspentLitecoinService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.*;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;


@Slf4j
@Component
public class LitecoinTransactionFactory {

    private final UnspentLitecoinService unspentService;

    public LitecoinTransactionFactory(UnspentLitecoinService unspentService) {
        this.unspentService = unspentService;
    }


    public Transaction createLitecoinTransaction(final LitecoinTransactionSignable signable, final String from) {
        try {
            final Address fromAddress = Address.fromBase58(new LitecoinParams(), from);
            final Address toAddress = Address.fromBase58(new LitecoinParams(), signable.getAddress());
            final long amountToSend = signable.getPhotonValue().longValue();


            Transaction transaction = new Transaction(new LitecoinParams());
            transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);
            transaction.addOutput(Coin.valueOf(amountToSend), toAddress);

            addInputsAndOutputsToTransaction(fromAddress, transaction, amountToSend, signable.getFeePerByte());

            transaction.verify();

            return transaction;
        } catch (ArkaneException ex) {
            throw ex;
        } catch (WrongNetworkException ex) {
            throw ArkaneException.arkaneException()
                    .errorCode("litecoin.address-wrong-network")
                    .message(ex.getMessage())
                    .build();
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            throw ArkaneException.arkaneException()
                    .errorCode("litecoin.creation-error")
                    .message(String.format("An error occurred trying to create the Litecoin transaction: %s", ex.getMessage()))
                    .build();
        }
    }


    private List<Unspent> fetchUnspents(final Address address) {
        final List<Unspent> unspentForAddress = unspentService.getUnspentForAddress(address.toBase58());
        if (unspentForAddress.isEmpty()) {
            throw ArkaneException.arkaneException()
                    .errorCode("litecoin.transaction-inputs")
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
                    .errorCode("litecoin.not-enough-funds")
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
        final TransactionOutPoint outPoint = new TransactionOutPoint(new LitecoinParams(), unspent.getVOut(), Sha256Hash.wrap(unspent.getTxId()));
        return new TransactionInput(new LitecoinParams(), tx, Hex.decodeHex(unspent.getScriptPubKey()), outPoint, Coin.valueOf(unspent.getAmount()));
    }
}
