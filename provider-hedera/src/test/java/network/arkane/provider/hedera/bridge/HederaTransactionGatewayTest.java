package network.arkane.provider.hedera.bridge;

import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.HbarTransferSignable;
import network.arkane.provider.hedera.sign.HbarTransferSigner;
import network.arkane.provider.hedera.sign.TokenAssociationSignable;
import network.arkane.provider.hedera.sign.TokenAssociationSigner;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

class HederaTransactionGatewayTest {

    private HbarTransferSigner signer;
    private HederaTransactionGateway transactionGateway;
    private TokenAssociationSigner tokenAssociationSigner;

    @BeforeEach
    void setUp() {
        HederaClientFactory clientFactory = HederaTestFixtures.clientFactory();
        signer = new HbarTransferSigner(clientFactory);
        tokenAssociationSigner = new TokenAssociationSigner(clientFactory);
        transactionGateway = new HederaTransactionGateway(clientFactory);
    }

    @Test
    void sign() {
        TransactionSignature signature = signer.createSignature(HbarTransferSignable.builder()
                                                                                    .from(HederaTestFixtures.getAccountId().toString())
                                                                                    .to("0.0.2256926")
                                                                                    .amount(new BigDecimal("1000"))
                                                                                    .build(),
                                                                HederaSecretKey.builder().key(HederaTestFixtures.getOperatorKey()).build());

        Signature result = transactionGateway.submit(signature, Optional.empty());

        System.out.println(result);
    }

    @Test
    void userTransaction() {
        TransactionSignature signature = signer.createSignature(HbarTransferSignable.builder()
                                                                                    .from("0.0.2256926")
                                                                                    .to("0.0.2151494")
                                                                                    .amount(new BigDecimal("0.01"))
                                                                                    .build(),
                                                                HederaSecretKey.builder()
                                                                               .key(PrivateKey.fromString(
                                                                                       "302e020100300506032b6570042204205b9ef9ca280c49f0080e7264ded9741d2689c2e8eab45d52105aa81f53426502"))
                                                                               .build());

        SubmittedAndSignedTransactionSignature result = transactionGateway.submit(signature, Optional.empty());

        System.out.println(result.getTransactionHash());
    }

    @Test
    void tokenAssociation() {
        TransactionSignature signature = tokenAssociationSigner.createSignature(TokenAssociationSignable.builder()
                                                                                                        .accountId("0.0.2256926")
                                                                                                        .tokenIds(Collections.singletonList("0.0.2256890"))
                                                                                                        .build(),
                                                                                HederaSecretKey.builder()
                                                                                               .key(PrivateKey.fromString(
                                                                                                       "302e020100300506032b6570042204205b9ef9ca280c49f0080e7264ded9741d2689c2e8eab45d52105aa81f53426502"))
                                                                                               .build());

        SubmittedAndSignedTransactionSignature result = transactionGateway.submit(signature, Optional.empty());

        System.out.println(result.getTransactionHash());
    }
}
