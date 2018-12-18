package network.arkane.provider.bridge;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;

public interface TransactionGateway {

    /**
     * Submit a signed transaction, given a TransactionSignature
     *
     * @param transactionSignature
     * @return
     */
    Signature submit(TransactionSignature transactionSignature);

    /**
     * The SecretType this specific TransactionGateway supports
     * @return
     */
    SecretType getType();
}
