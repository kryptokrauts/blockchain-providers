package network.arkane.provider.hedera.mirror.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class HederaTransaction {
    private String charged_tx_fee;
    private String consensus_timestamp;
    private String entity_id;
    private String max_fee;
    private String memo_base64;
    private String name;
    private String node;
    private String result;
    private boolean scheduled;
    private List<HederaTokenTransfer> token_transfers;
    private String transaction_hash;
    private String transaction_id;
    private List<HederaTransfer> transfers;
    private String valid_duration_seconds;
    private String valid_start_timestamp;
}
