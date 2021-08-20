package network.arkane.provider.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.NetworkAddressBookFix;
import com.hedera.hashgraph.sdk.PrivateKey;
import org.springframework.stereotype.Component;

@Component
public class HederaClientFactory {

    static {
        NetworkAddressBookFix.initializeEmptyAddressBook();
    }

    private final HederaProperties hederaProperties;
    private final Client clientWithOperator;

    public HederaClientFactory(HederaProperties hederaProperties) {
        this.hederaProperties = hederaProperties;
        this.clientWithOperator = buildClient(hederaProperties.getOperatorAccountId(), hederaProperties.getOperatorPrivateKey());
    }

    public Client getClientWithOperator() {
        return clientWithOperator;
    }

    public Client buildClient(String operatorId,
                              String operatorPrivateKey) {
        return buildClient(AccountId.fromString(operatorId), PrivateKey.fromString(operatorPrivateKey));
    }

    public Client buildClient(AccountId operatorId,
                              PrivateKey operatorPrivateKey) {
        return buildClient(this.hederaProperties).setOperator(operatorId,
                                                              operatorPrivateKey);
    }

    private Client buildClient(HederaProperties properties) {

        switch (properties.getNetwork().toLowerCase()) {
            case "previewnet":
                return Client.forPreviewnet();
            case "testnet":
                return Client.forTestnet();
            default:
                return Client.forMainnet();
        }
    }

}
