package network.arkane.provider.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HederaAutoConfiguration {

    @Bean
    public Client hederaClient(HederaProperties properties) {
        return hederaClient(properties);
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

    @Bean
    public Client hederaClientWithOperator(HederaProperties properties) {
        return buildClient(properties).setOperator(AccountId.fromString("0.0.1543821"),
                                                   PrivateKey.fromString("302e020100300506032b6570042204202c112db3951f5de38d47196883797d74efe064fa68fa2931dda6c1a0e8c848d1"));
    }

}
