package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;

import java.util.Map;

public interface TransactionInfoService {
    SecretType type();

    TxInfo getTransaction(String hash);

    default TxInfo getTransaction(String hash,
                                  Map<String, Object> parameters) {
        return getTransaction(hash);
    }

}
