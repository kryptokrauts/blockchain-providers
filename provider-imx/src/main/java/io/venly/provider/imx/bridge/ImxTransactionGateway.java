package io.venly.provider.imx.bridge;

import io.venly.provider.imx.ImxGatewayClient;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ImxTransactionGateway implements TransactionGateway {

    private final ImxGatewayClient imxGatewayClient;

    public ImxTransactionGateway(ImxGatewayClient imxGatewayClient) {
        this.imxGatewayClient = imxGatewayClient;
    }

    @Override
    public Signature submit(TransactionSignature transactionSignature, Optional<String> endpoint) {
        return imxGatewayClient.get("/api/transactions", SubmittedAndSignedTransactionSignature.class);
    }

    @Override
    public SecretType getType() {
        return SecretType.IMX;
    }


}
