package network.arkane.provider.aeternity.blockchain;

import com.kryptokrauts.aeternity.sdk.service.aeternity.impl.AeternityService;
import network.arkane.provider.blockchain.BlockchainInfoService;
import network.arkane.provider.chain.SecretType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class AeternityBlockchainInfoService implements BlockchainInfoService {

    private AeternityService aeternityService;

    public AeternityBlockchainInfoService(final @Qualifier("aeternity-service") AeternityService aeternityService) {
        this.aeternityService = aeternityService;
    }

    @Override
    public SecretType type() {
        return SecretType.AETERNITY;
    }

    @Override
    public BigInteger getBlockNumber() {
        return this.aeternityService.info.blockingGetCurrentKeyBlock().getHeight();
    }
}
