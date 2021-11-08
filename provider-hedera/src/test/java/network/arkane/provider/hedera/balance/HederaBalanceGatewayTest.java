package network.arkane.provider.hedera.balance;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.TransactionResponse;
import network.arkane.provider.balance.domain.Balance;
import network.arkane.provider.balance.domain.TokenBalance;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.HederaProperties;
import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Disabled
class HederaBalanceGatewayTest {

    private HederaBalanceGateway hederaBalanceGateway;

    @BeforeEach
    void setUp() {
        HederaClientFactory clientFactory = HederaTestFixtures.clientFactory();
        hederaBalanceGateway = new HederaBalanceGateway(clientFactory,
                                                        new HederaTokenInfoService(clientFactory, HederaTestFixtures.mirrorNodeClient()),
                                                        new MirrorNodeClient(HederaProperties.builder()
                                                                                             .mirrorNodeApiEndpoint("https://testnet.mirrornode.hedera.com/api/v1")
                                                                                             .build()));
    }

    @Test
    void getBalance() {
        Balance balance = hederaBalanceGateway.getBalance(HederaTestFixtures.getAccountId().toString());

        System.out.println(balance);

    }

    @Test
    void getTokenBalance() {
        List<TokenBalance> balance = hederaBalanceGateway.getTokenBalances("0.0.2258392");

        System.out.println(balance);

    }

    @Test
    void createNft() throws PrecheckStatusException, TimeoutException {
        TransactionResponse response = new TokenCreateTransaction()
                .setTokenType(TokenType.NON_FUNGIBLE_UNIQUE)
                .setInitialSupply(0)
                .setTreasuryAccountId(HederaTestFixtures.getAccountId())
                .setDecimals(0)
                .setTokenName("Digimon")
                .setTokenMemo("Digimon world")
                .setTokenSymbol("DGM")
                .setSupplyKey(HederaTestFixtures.getOperatorKey())
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
        System.out.println(response);
    }

    @Test
    void mintNft() throws PrecheckStatusException, TimeoutException {
        //nft token id: 	0.0.2849838
        TransactionResponse response = new TokenMintTransaction()
                .setTokenId(TokenId.fromString("0.0.2850147"))
                .setMetadata(Collections.singletonList("https://bit.ly/3DCrdQD".getBytes()))
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
        System.out.println(response);
    }

    @Test
    void tokenBalances() throws PrecheckStatusException, TimeoutException {
        //nft token id: 	0.0.2849838
        AccountBalance response = new AccountBalanceQuery()
                .setAccountId(HederaTestFixtures.getAccountId())
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
        System.out.println(response);
    }

    @Test
    void nftBalances() throws PrecheckStatusException, TimeoutException {
        //nft token id: 	0.0.2849838
        AccountBalance response = new AccountBalanceQuery()
                .setAccountId(HederaTestFixtures.getAccountId())
                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
        System.out.println(response);
    }
    //
    //    @Test
    //    void getNftInfo() throws PrecheckStatusException, TimeoutException {
    //        //nft token id: 	0.0.2849838
    //        TransactionResponse response = new TokenNftInfoQuery()
    //                .set(TokenId.fromString("0.0.2849838"))
    //                .setMetadata(Collections.singletonList("https://metadata.arkane.network/api/apps/00608b13-4343-4234-8228-c094cc4ee2da/contracts/78/token-types/1/metadata".getBytes()))
    //                .execute(HederaTestFixtures.clientFactory().getClientWithOperator());
    //        System.out.println(response);
    //    }
}
