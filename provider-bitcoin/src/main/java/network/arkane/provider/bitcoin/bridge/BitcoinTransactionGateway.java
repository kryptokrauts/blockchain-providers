package network.arkane.provider.bitcoin.bridge;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BitcoinTransactionGateway implements TransactionGateway {

    private BlockcypherGateway blockcypherGateway;
    private BitcoinEnv bitcoinEnv;

    public BitcoinTransactionGateway(BlockcypherGateway blockcypherGateway, BitcoinEnv bitcoinEnv) {
        this.blockcypherGateway = blockcypherGateway;
        this.bitcoinEnv = bitcoinEnv;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, final Optional<String> endpoint) {
        BlockCypherRawTransactionResponse submission = blockcypherGateway.sendSignedTransaction(bitcoinEnv.getNetwork(), transactionSignature.getSignedTransaction());
        return new SubmittedAndSignedTransactionSignature(submission.getTransactionId(), transactionSignature.getSignedTransaction());
    }

    @Override
    public SecretType getType() {
        return SecretType.BITCOIN;
    }
}
