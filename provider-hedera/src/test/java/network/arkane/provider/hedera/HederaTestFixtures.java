package network.arkane.provider.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;

public final class HederaTestFixtures {
    private HederaTestFixtures() {
    }

    public static AccountId getAccountId() {
        return AccountId.fromString("0.0.1543821");
    }

    public static PrivateKey getOperatorKey() {
        return PrivateKey.fromString("302e020100300506032b6570042204202c112db3951f5de38d47196883797d74efe064fa68fa2931dda6c1a0e8c848d1");
    }

    public static HederaClientFactory clientFactory() {
        return new HederaClientFactory(HederaProperties.builder()
                                                       .network("testnet")
                                                       .operatorAccountId("0.0.1543821")
                                                       .operatorPrivateKey("302e020100300506032b6570042204202c112db3951f5de38d47196883797d74efe064fa68fa2931dda6c1a0e8c848d1")
                                                       .build());
    }

    public static MirrorNodeClient mirrorNodeClient() {
        return new MirrorNodeClient(getHederaProperties());
    }

    private static HederaProperties getHederaProperties() {
        return HederaProperties.builder()
                               .operatorAccountId(getAccountId().toString())
                               .operatorPrivateKey(getOperatorKey().toString())
                               .mirrorNodeApiEndpoint("https://testnet.mirrornode.hedera.com/api/v1")
                               .build();
    }
}
