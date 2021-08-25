package network.arkane.provider.hedera.mirror;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.arkane.provider.hedera.HederaProperties;
import network.arkane.provider.hedera.balance.dto.HederaTokenInfo;
import network.arkane.provider.hedera.mirror.dto.Accounts;
import network.arkane.provider.hedera.mirror.dto.Balances;
import network.arkane.provider.hedera.mirror.dto.HederaTransactions;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Component
public class MirrorNodeClient {
    private final RestTemplate restTemplate;


    public MirrorNodeClient(HederaProperties properties) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultUriBuilderFactory defaultUriTemplateHandler = new DefaultUriBuilderFactory(properties.getMirrorNodeApiEndpoint());
        restTemplate = new RestTemplateBuilder().defaultMessageConverters()
                                                .uriTemplateHandler(defaultUriTemplateHandler)
                                                .setConnectTimeout(Duration.ofSeconds(5))
                                                .additionalInterceptors((request, body, execution) -> {
                                                    request.getHeaders().set("User-Agent", "curl/7.54.0/venly.mirrornode");
                                                    return execution.execute(request, body);
                                                })
                                                .setReadTimeout(Duration.ofSeconds(60))
                                                .build();
    }

    public Accounts getAccounts(String publicKey) {
        return restTemplate.getForObject("/accounts?account.publickey={publicKey}", Accounts.class, publicKey);
    }

    public Balances getBalance(String accountId) {
        return restTemplate.getForObject("/balances?account.id={accountId}&limit=1", Balances.class, accountId);
    }

    public HederaTokenInfo getTokenInfo(String tokenId) {
        return restTemplate.getForObject("/tokens/{tokenId}", HederaTokenInfo.class, tokenId);
    }

    public HederaTransactions getTxStatus(String transactionId) {
        String[] splitted = transactionId.split("@");
        String newTxId = splitted[0] + "-" + splitted[1].replaceAll("\\.", "-");
        return restTemplate.getForObject("/transactions/{txId}", HederaTransactions.class, newTxId);
    }
}
