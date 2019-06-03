package network.arkane.provider.litecoin.bridge;

import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LitecoinTransactionGateway implements TransactionGateway {

    private final LitecoinEnv litecoinEnv;
    private final BlockcypherGateway blockcypherGateway;

    public LitecoinTransactionGateway(LitecoinEnv litecoinEnv, BlockcypherGateway blockcypherGateway) {
        this.litecoinEnv = litecoinEnv;
        this.blockcypherGateway = blockcypherGateway;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, final Optional<String> endpoint) {
        BlockCypherRawTransactionResponse response = blockcypherGateway.sendSignedTransaction(
                litecoinEnv.getNetwork(),
                transactionSignature.getSignedTransaction()
        );

        return new SubmittedAndSignedTransactionSignature(response.getTransactionId(), transactionSignature.getSignedTransaction());
    }

    @Override
    public SecretType getType() {
        return SecretType.LITECOIN;
    }
}
