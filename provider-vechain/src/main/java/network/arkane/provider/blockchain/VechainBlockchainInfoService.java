package network.arkane.provider.blockchain;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.gateway.VechainGateway;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class VechainBlockchainInfoService implements BlockchainInfoService {

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

}
