package network.arkane.provider.aeternity.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class AeternityTransactionInfoService implements TransactionInfoService {


    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        throw new NotImplementedException("This feature is not available yet for " + type().name());
    }
}
