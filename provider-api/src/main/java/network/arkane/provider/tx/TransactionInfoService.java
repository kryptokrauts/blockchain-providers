package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;

public interface TransactionInfoService {
    SecretType type();

    TxInfo getTransaction(String hash);
}
