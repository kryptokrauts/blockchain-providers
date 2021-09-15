package network.arkane.provider.hedera.blockchain;

import network.arkane.provider.blockchain.BlockchainInfoService;
import network.arkane.provider.chain.SecretType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class HederaBlockchainInfoService implements BlockchainInfoService {

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }

    @Override
    public BigInteger getBlockNumber() {
        throw new NotImplementedException("Feature not implemented for " + type().name());
    }
}
