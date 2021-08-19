package network.arkane.provider.hedera.bridge;

import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.HbarTransferSignable;
import network.arkane.provider.hedera.sign.HbarTransferSigner;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

class HederaTransactionGatewayTest {

    private HbarTransferSigner signer;
    private HederaTransactionGateway transactionGateway;

    @BeforeEach
    void setUp() {
        HederaClientFactory clientFactory = HederaTestFixtures.clientFactory();
        signer = new HbarTransferSigner(clientFactory);
        transactionGateway = new HederaTransactionGateway(clientFactory);
    }

    @Test
    void sign() {
        TransactionSignature signature = signer.createSignature(HbarTransferSignable.builder()
                                                                                    .from(HederaTestFixtures.getAccountId().toString())
                                                                                    .to("0.0.2151494")
                                                                                    .amount(new BigDecimal("0.01"))
                                                                                    .build(),
                                                                HederaSecretKey.builder().key(HederaTestFixtures.getOperatorKey()).build());

        Signature result = transactionGateway.submit(signature, Optional.empty());

        System.out.println(result);
    }

}
