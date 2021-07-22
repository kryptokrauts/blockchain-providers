package network.arkane.provider.hedera.wallet;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class HederaWalletGeneratorTest {

    private HederaWalletGenerator hederaWalletGenerator;

    @BeforeEach
    void setUp() {
        Client client = Client.forTestnet().setOperator(AccountId.fromString("0.0.1543821"),
                                                        PrivateKey.fromString("302e020100300506032b6570042204202c112db3951f5de38d47196883797d74efe064fa68fa2931dda6c1a0e8c848d1"));
        hederaWalletGenerator = new HederaWalletGenerator(client);
    }

    @Test
    void generateWallet() {
        GeneratedWallet generatedWallet = hederaWalletGenerator.generateWallet("password", HederaSecretKey.builder().key(PrivateKey.generate()).build());
    }
}
