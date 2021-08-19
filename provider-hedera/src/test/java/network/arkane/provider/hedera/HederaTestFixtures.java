package network.arkane.provider.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.NetworkAddressBookFix;
import com.hedera.hashgraph.sdk.PrivateKey;

public final class HederaTestFixtures {
    private HederaTestFixtures() {
    }

    public static AccountId getAccountId() {
        return AccountId.fromString("0.0.1543821");
    }

    public static PrivateKey getOperatorKey() {
        return PrivateKey.fromString("302e020100300506032b6570042204202c112db3951f5de38d47196883797d74efe064fa68fa2931dda6c1a0e8c848d1");
    }

    public static Client testClient() {
        NetworkAddressBookFix.initializeEmptyAddressBook();
        return Client.forTestnet().setOperator(getAccountId(), getOperatorKey());
    }
}
