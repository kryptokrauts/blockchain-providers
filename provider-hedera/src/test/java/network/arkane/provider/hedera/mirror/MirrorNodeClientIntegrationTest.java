package network.arkane.provider.hedera.mirror;

import network.arkane.provider.hedera.HederaTestFixtures;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.dto.Accounts;
import network.arkane.provider.hedera.mirror.dto.MirrorNodeNft;
import network.arkane.provider.hedera.mirror.dto.NftWalletDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class MirrorNodeClientIntegrationTest {

    private MirrorNodeClient mirrorNodeClient;

    @BeforeEach
    void setUp() {
        mirrorNodeClient = HederaTestFixtures.mirrorNodeClient();
    }

    @Test
    void getAccounts() {
        Accounts accounts = mirrorNodeClient.getAccounts(HederaTestFixtures.clientFactory().getClientWithOperator().getOperatorPublicKey().toString());

        System.out.println(accounts);
    }

    @Test
    void getTokenInfo() {
        HederaTokenInfo tokenInfo = mirrorNodeClient.getTokenInfo("0.0.2517364");

        System.out.println(tokenInfo);
    }

    @Test
    void getTokenNftInfo() {
        HederaTokenInfo tokenInfo = mirrorNodeClient.getTokenInfo("0.0.2850147");

        System.out.println(tokenInfo);
    }

    @Test
    void getNfts() {
        List<MirrorNodeNft> nfts = mirrorNodeClient.getNfts("0.0.2850147", "0.0.1543821");

        System.out.println(nfts);
    }

    @Test
    void getEmptyNfts() {
        List<MirrorNodeNft> nfts = mirrorNodeClient.getNfts("0.0.2850147", "0.0.2258392");

        System.out.println(nfts);
    }

    @Test
    void getEmptyNftWallets() {
        List<NftWalletDto> nftWallets = mirrorNodeClient.getNftWallets("0.0.2850147", null);

        System.out.println(nftWallets);
    }
}
