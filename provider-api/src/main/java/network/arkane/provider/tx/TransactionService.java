package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;

public interface TransactionService {
    SecretType type();

    TxInfo getTransaction(String hash);
}
