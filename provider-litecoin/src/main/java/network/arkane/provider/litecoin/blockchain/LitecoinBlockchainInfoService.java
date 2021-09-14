package network.arkane.provider.litecoin.blockchain;

import network.arkane.provider.blockchain.BlockchainInfoService;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.litecoin.LitecoinEnv;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class LitecoinBlockchainInfoService implements BlockchainInfoService {

    private final LitecoinEnv litecoinEnv;
    private final BlockcypherGateway blockcypherGateway;

    public LitecoinBlockchainInfoService(LitecoinEnv litecoinEnv,
                                         BlockcypherGateway blockcypherGateway) {
        this.litecoinEnv = litecoinEnv;
        this.blockcypherGateway = blockcypherGateway;
    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }

    @Override
    public BigInteger getBlockNumber() {
        return blockcypherGateway.getBlockchain(litecoinEnv.getNetwork())
                                 .getHeight();
    }
}
