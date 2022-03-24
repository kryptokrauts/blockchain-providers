package network.arkane.provider.hedera.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HederaTransactionReceipt {
    private String status;

    private String accountId;

    private String fileId;

    private String contractId;

    private String tokenId;

    private String topicId;

    private Long topicSequenceNumber;

    private String topicRunningHash;

    private Long totalSupply;

    private String scheduleId;

    private String scheduledTransactionId;

    private List<Long> serials;
}
