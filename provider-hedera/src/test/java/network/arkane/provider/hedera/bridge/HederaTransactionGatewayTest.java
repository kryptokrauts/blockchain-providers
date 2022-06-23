package network.arkane.provider.hedera.bridge;

import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.HbarSingleTransferSigner;
import network.arkane.provider.hedera.sign.HbarTransferSignable;
import network.arkane.provider.hedera.sign.TokenAssociationSignable;
import network.arkane.provider.hedera.sign.TokenAssociationSigner;
import network.arkane.provider.hedera.sign.TokenSingleTransferSigner;
import network.arkane.provider.hedera.sign.TokenTransferSignable;
import network.arkane.provider.hedera.sign.handler.HbarTransferHandler;
import network.arkane.provider.hedera.sign.handler.TokenTransferHandler;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Disabled
class HederaTransactionGatewayTest {

    private HbarSingleTransferSigner signer;
    private HederaTransactionGateway transactionGateway;
    private TokenAssociationSigner tokenAssociationSigner;
    private TokenSingleTransferSigner tokenTransferSigner;

    @BeforeEach
    void setUp() {
        HederaClientFactory clientFactory = HederaTestFixtures.clientFactory();
        signer = new HbarSingleTransferSigner(clientFactory, new HbarTransferHandler());
        tokenAssociationSigner = new TokenAssociationSigner(clientFactory);
        transactionGateway = new HederaTransactionGateway(clientFactory);
        tokenTransferSigner = new TokenSingleTransferSigner(clientFactory, new TokenTransferHandler());
    }

    @Test
    void sign() {
        TransactionSignature signature = signer.createSignature(HbarTransferSignable.builder()
                                                                                    .from(HederaTestFixtures.getAccountId().toString())
                                                                                    .to("0.0.2256926")
                                                                                    .amount(new BigInteger("1000"))
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
                                                                                    .amount(new BigInteger("1"))
                                                                                    .build(),
                                                                HederaSecretKey.builder()
                                                                               .key(PrivateKey.fromString(
                                                                                       "302e020100300506032b6570042204205b9ef9ca280c49f0080e7264ded9741d2689c2e8eab45d52105aa81f53426502"))
                                                                               .build());

        SubmittedAndSignedTransactionSignature result = transactionGateway.submit(signature, Optional.empty());

        System.out.println(result.getTransactionHash());
    }

    @Test
    void userTokenTransaction() {
        TransactionSignature signature = tokenTransferSigner.createSignature(TokenTransferSignable.builder()
                                                                                                  .from("0.0.1543821")
                                                                                                  .to("0.0.2562819")
                                                                                                  .tokenId("0.0.2517364")
                                                                                                  .amount(new BigInteger("100000000000"))
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

    @Test
    void createToken() throws PrecheckStatusException, TimeoutException {
        TransactionResponse response = new TokenCreateTransaction()
                .setTokenName("Banana")
                .setTokenSymbol("BNA")
                .setDecimals(8)
                .setTokenType(TokenType.FUNGIBLE_COMMON)
                .setInitialSupply(100000000000000000L)
                .setAutoRenewAccountId(HederaTestFixtures.getAccountId())
                .setTokenMemo("Banana tokens from Venly")
                .setTreasuryAccountId(HederaTestFixtures.getAccountId())
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());

        System.out.println(response);
    }

    @Test
    void mintToken() throws PrecheckStatusException, TimeoutException {
        final String metadata
                = "{\"name\":\"Banana\",\"description\":\"Banana tokens from Venly\",\"image\":\"https://dev.luthersystemsapp"
                  + ".com/chloe_assets/SearchingForLightNo_81_20/image_part_005.jpg\",\"localization\":{\"uri\":\"https://dev.luthersystemsapp.com/nft-test-{locale}.json\","
                  + "\"default\":\"en\",\"locales\":[\"en\",\"es\",\"fr\"]}}";

        TransactionResponse response = new TokenMintTransaction()
                .setTokenId(TokenId.fromString("0.0.2268875"))
                .setAmount(1500)
                .setMetadata(Collections.singletonList(metadata.getBytes(StandardCharsets.UTF_8)))
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
        System.out.println(response);
    }
}
