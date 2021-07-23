package network.arkane.provider.hedera.tx;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionReceiptQuery;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
public class HederaTransactionInfoService implements TransactionInfoService {

    private final Client hederaClient;

    public HederaTransactionInfoService(Client hederaClient) {
        this.hederaClient = hederaClient;
    }

    public SecretType type() {
        return SecretType.HEDERA;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        try {
            TransactionReceipt receipt = new TransactionReceiptQuery()
                    .setTransactionId(TransactionId.fromString(hash))
                    .execute(hederaClient, Duration.of(5, ChronoUnit.SECONDS));
            System.out.println(receipt);
        } catch (TimeoutException | PrecheckStatusException e) {
            throw arkaneException()
                    .errorCode("error.hedera.receipt")
                    .message("Error getting hedera transaction receipt")
                    .cause(e)
                    .build();
        }
        return TxInfo.builder()
                     .build();
    }
}
