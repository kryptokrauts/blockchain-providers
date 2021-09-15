package network.arkane.provider.neo.blockchain;

import network.arkane.provider.blockchain.BlockchainInfoService;
import network.arkane.provider.chain.SecretType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class NeoBlockchainInfoService implements BlockchainInfoService {


    public SecretType type() {
        return SecretType.NEO;
    }

    @Override
    public BigInteger getBlockNumber() {
        throw new NotImplementedException("This feature is not available yet for " + type().name());
    }

}
