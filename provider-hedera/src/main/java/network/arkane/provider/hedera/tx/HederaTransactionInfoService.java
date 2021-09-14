package network.arkane.provider.hedera.tx;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.hedera.mirror.dto.HederaTransaction;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Component
public class HederaTransactionInfoService implements TransactionInfoService {

    private final MirrorNodeClient mirrorNodeClient;

    public HederaTransactionInfoService(MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = mirrorNodeClient;
    }


    public SecretType type() {
        return SecretType.HEDERA;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        try {
            List<HederaTransaction> transactions = mirrorNodeClient.getTxStatus(hash).getTransactions();
            if (transactions.size() > 0) {
                HederaTransaction hederaTransaction = transactions.get(0);
                return HederaTxInfo.hederaTxInfoBuilder()
                                   .hash(hederaTransaction.getTransaction_hash())
                                   .status(getStatus(hederaTransaction.getResult()))
                                   .chargedTxFee(hederaTransaction.getCharged_tx_fee())
                                   .consensusTimestamp(hederaTransaction.getConsensus_timestamp())
                                   .entityId(hederaTransaction.getEntity_id())
                                   .maxFee(hederaTransaction.getMax_fee())
                                   .memoBase64(hederaTransaction.getMemo_base64())
                                   .name(hederaTransaction.getName())
                                   .node(hederaTransaction.getNode())
                                   .scheduled(hederaTransaction.isScheduled())
                                   .transactionId(hederaTransaction.getTransaction_id())
                                   .validDurationSeconds(hederaTransaction.getValid_duration_seconds())
                                   .validStartTimestamp(hederaTransaction.getValid_start_timestamp())
                                   .transfers(hederaTransaction.getTransfers())
                                   .tokenTransfers(hederaTransaction.getToken_transfers())
                                   .result(hederaTransaction.getResult())
                                   .build();
            }

        } catch (Exception e) {
            throw arkaneException()
                    .errorCode("error.hedera.receipt")
                    .message("Error getting hedera transaction receipt")
                    .cause(e)
                    .build();
        }
        return TxInfo.builder()
                     .build();
    }

    private TxStatus getStatus(String result) {
        if (StringUtils.isBlank(result)) {
            return TxStatus.UNKNOWN;
        } else if ("SUCCESS".equals(result)) {
            return TxStatus.SUCCEEDED;
        }
        return TxStatus.FAILED;
    }
}
