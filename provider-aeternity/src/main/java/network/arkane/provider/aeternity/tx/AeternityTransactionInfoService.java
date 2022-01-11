package network.arkane.provider.aeternity.tx;

import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import com.kryptokrauts.aeternity.sdk.service.info.domain.KeyBlockResult;
import com.kryptokrauts.aeternity.sdk.service.info.domain.TransactionResult;
import java.math.BigInteger;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AeternityTransactionInfoService implements TransactionInfoService {

    private AeternityService aeternityService;

    public AeternityTransactionInfoService(final @Qualifier("aeternity-service") AeternityService aeternityService) {
        this.aeternityService = aeternityService;
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        TransactionResult txResult = this.aeternityService.info.blockingGetTransactionByHash(hash);
        BigInteger confirmations = BigInteger.ZERO;
        Boolean reachedFinality = false;
        TxStatus txStatus;
        if (txResult.getBlockHeight() != null && txResult.getBlockHeight().compareTo(BigInteger.valueOf(-1)) == 0) {
            txStatus = TxStatus.PENDING;
        } else if(txResult.getBlockHeight() != null && txResult.getBlockHeight().compareTo(BigInteger.valueOf(0)) == 1) {
            KeyBlockResult keyBlockResult = this.aeternityService.info.blockingGetCurrentKeyBlock();
            confirmations = keyBlockResult.getHeight().subtract(txResult.getBlockHeight());
            // we assume finality to be reached after 100 keyblocks
            // see https://github.com/aeternity/aeternity/blob/master/docs/fork-resistance.md
            reachedFinality = confirmations.compareTo(BigInteger.valueOf(100)) == 1;
            txStatus = TxStatus.SUCCEEDED;
        } else {
            txStatus = TxStatus.UNKNOWN;
        }
        return TxInfo.builder()
                .blockHash(txResult.getBlockHash())
                .blockNumber(txResult.getBlockHeight())
                .confirmations(confirmations)
                .hash(txResult.getHash() == null ? hash : txResult.getHash())
                .hasReachedFinality(reachedFinality)
                .status(txStatus).build();
    }
}
