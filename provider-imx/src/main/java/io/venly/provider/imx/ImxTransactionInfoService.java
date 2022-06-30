package io.venly.provider.imx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;

public class ImxTransactionInfoService implements TransactionInfoService {

    private final ImxTransactionGateway imxTransactionGateway;

    public ImxTransactionInfoService(ImxTransactionGateway imxTransactionGateway) {
        this.imxTransactionGateway = imxTransactionGateway;
    }

    @Override
    public SecretType type() {
        return SecretType.IMX;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        return this.imxTransactionGateway.getStatusInfo(hash);
    }
}
