package network.arkane.provider.hedera.tx;

import lombok.Builder;
import network.arkane.provider.hedera.mirror.dto.HederaTokenTransfer;
import network.arkane.provider.hedera.mirror.dto.HederaTransfer;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;

import java.util.List;

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
    private List<HederaTransfer> transfers;
    private List<HederaTokenTransfer> tokenTransfers;

    @Builder(builderMethodName = "hederaTxInfoBuilder")
    public HederaTxInfo(String hash,
                        TxStatus status,
                        String chargedTxFee,
                        String consensusTimestamp,
                        String entityId,
                        String maxFee,
                        String memoBase64,
                        String name,
                        String node,
                        boolean scheduled,
                        String transactionId,
                        String validDurationSeconds,
                        String validStartTimestamp,
                        List<HederaTransfer> transfers,
                        List<HederaTokenTransfer> tokenTransfers) {
        super(hash, status, null, null, null);
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
        this.transfers = transfers;
        this.tokenTransfers = tokenTransfers;
    }
}
