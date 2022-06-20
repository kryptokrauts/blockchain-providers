package io.venly.provider.imx.bridge;

import io.venly.provider.imx.ImxGatewayClient;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionProviderGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImxTransactionGateway extends TransactionProviderGateway<SubmittedAndSignedTransactionSignature> {

    public ImxTransactionGateway(final ImxGatewayClient imxGatewayClient) {
        super(imxGatewayClient, SecretType.IMX, SubmittedAndSignedTransactionSignature.class);
    }
}
