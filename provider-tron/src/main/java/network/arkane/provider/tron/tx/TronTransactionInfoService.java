package network.arkane.provider.tron.tx;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ChainNoLongerSupportedException;
import network.arkane.provider.tx.TransactionInfoService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TronTransactionInfoService implements TransactionInfoService {

    public SecretType type() {
        return SecretType.TRON;
    }

    @Override
    public TronTxInfo getTransaction(String hash) {
        throw new ChainNoLongerSupportedException();
    }
}
