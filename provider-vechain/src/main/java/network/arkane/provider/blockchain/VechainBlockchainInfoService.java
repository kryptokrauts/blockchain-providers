package network.arkane.provider.blockchain;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.VechainGateway;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class VechainBlockchainInfoService implements BlockchainInfoService, DisposableBean {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder().setNameFormat("vechain-tx-info-%d").build());
    private final VechainGateway vechainGateway;

    public VechainBlockchainInfoService(VechainGateway vechainGateway) {
        this.vechainGateway = vechainGateway;
    }

    public SecretType type() {
        return SecretType.VECHAIN;
    }

    @Override
    public BigInteger getBlockNumber() {
        String blockNumber = vechainGateway.getBlock()
                                           .getNumber();
        return blockNumber != null ? new BigInteger(blockNumber) : null;
    }

    public void destroy() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
