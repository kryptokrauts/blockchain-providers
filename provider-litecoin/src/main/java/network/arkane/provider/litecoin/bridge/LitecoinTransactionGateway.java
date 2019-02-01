package network.arkane.provider.litecoin.bridge;

import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

@Component
public class LitecoinTransactionGateway implements TransactionGateway {

    private final BlockcypherGateway blockcypherGateway;

    public LitecoinTransactionGateway(BlockcypherGateway blockcypherGateway) {
        this.blockcypherGateway = blockcypherGateway;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature) {
        BlockCypherRawTransactionResponse response = blockcypherGateway.sendSignedTransaction(Network.LITECOIN, transactionSignature.getSignedTransaction());

        return new SubmittedAndSignedTransactionSignature(response.getTransactionId(), transactionSignature.getSignedTransaction());
    }

    @Override
    public SecretType getType() {
        return SecretType.LITECOIN;
    }
}
