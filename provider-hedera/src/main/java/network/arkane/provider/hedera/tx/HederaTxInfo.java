package network.arkane.provider.hedera.tx;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import network.arkane.provider.hedera.mirror.dto.HederaTokenTransfer;
import network.arkane.provider.hedera.mirror.dto.HederaTransfer;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HederaTxInfo extends TxInfo {
    private String chargedTxFee;
    private String consensusTimestamp;
    private String entityId;
    private String maxFee;
    private String memoBase64;
    private String name;
    private String node;
    private boolean scheduled;
    private String transactionId;
    private String validDurationSeconds;
    private String validStartTimestamp;
    private String result;
    private List<HederaTransfer> transfers;
    private List<HederaTokenTransfer> tokenTransfers;

    @Builder(builderMethodName = "hederaTxInfoBuilder")
    public HederaTxInfo(final String hash,
                        final TxStatus status,
                        final String chargedTxFee,
                        final String consensusTimestamp,
                        final String entityId,
                        final String maxFee,
                        final String memoBase64,
                        final String name,
                        final String node,
                        final boolean scheduled,
                        final String transactionId,
                        final String validDurationSeconds,
                        final String validStartTimestamp,
                        final String result,
                        final List<HederaTransfer> transfers,
                        final List<HederaTokenTransfer> tokenTransfers) {
        super(hash, status, null, null, null, StringUtils.isNotBlank(consensusTimestamp));
        this.chargedTxFee = chargedTxFee;
        this.consensusTimestamp = consensusTimestamp;
        this.entityId = entityId;
        this.maxFee = maxFee;
        this.memoBase64 = memoBase64;
        this.name = name;
        this.node = node;
        this.scheduled = scheduled;
        this.transactionId = transactionId;
        this.validDurationSeconds = validDurationSeconds;
        this.validStartTimestamp = validStartTimestamp;
        this.result = result;
        this.transfers = transfers;
        this.tokenTransfers = tokenTransfers;
    }
}
