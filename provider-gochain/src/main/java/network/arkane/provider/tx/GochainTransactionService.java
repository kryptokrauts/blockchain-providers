package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class GochainTransactionService implements TransactionService {


    public SecretType type() {
        return SecretType.GOCHAIN;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        throw new NotImplementedException("This feature is not available yet for " + type().name());
    }
}
