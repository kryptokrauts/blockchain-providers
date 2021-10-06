package network.arkane.provider.bitcoin.tx;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.domain.TX;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.tx.HasReachedFinalityService;
import network.arkane.provider.tx.TransactionInfoService;
import network.arkane.provider.tx.TxInfo;
import network.arkane.provider.tx.TxStatus;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static network.arkane.provider.chain.SecretType.BITCOIN;
import static network.arkane.provider.tx.TxStatus.PENDING;
import static network.arkane.provider.tx.TxStatus.SUCCEEDED;
import static network.arkane.provider.tx.TxStatus.UNKNOWN;

@Component
public class BitcoinTransactionInfoService implements TransactionInfoService {

    private final BitcoinEnv bitcoinEnv;
    private final BlockcypherGateway blockcypherGateway;
    private final HasReachedFinalityService hasReachedFinalityService;

    public BitcoinTransactionInfoService(final BitcoinEnv bitcoinEnv,
                                         final BlockcypherGateway blockcypherGateway,
                                         final HasReachedFinalityService hasReachedFinalityService) {
        this.bitcoinEnv = bitcoinEnv;
        this.blockcypherGateway = blockcypherGateway;
        this.hasReachedFinalityService = hasReachedFinalityService;
    }

    public SecretType type() {
        return BITCOIN;
    }

    @Override
    public TxInfo getTransaction(String hash) {
        TX tx = blockcypherGateway.getTransactionByHash(bitcoinEnv.getNetwork(), hash);
        if (tx == null) return TxInfo.builder().status(UNKNOWN).build();
        return this.mapTxInfo(tx);
    }

    private TxInfo mapTxInfo(TX tx) {
        return TxInfo.builder()
                     .blockHash(tx.getBlockHash())
                     .blockNumber(tx.getBlockHeight())
                     .confirmations(tx.getConfirmations())
                     .hasReachedFinality(hasReachedFinalityService.hasReachedFinality(BITCOIN, tx.getConfirmations()))
                     .hash(tx.getHash())
                     .status(this.getStatus(tx))
                     .build();
    }

    private TxStatus getStatus(TX tx) {
        return (tx.getBlockHeight() != null && tx.getBlockHeight().compareTo(BigInteger.ONE) > 0) ? SUCCEEDED : PENDING;
    }
}
