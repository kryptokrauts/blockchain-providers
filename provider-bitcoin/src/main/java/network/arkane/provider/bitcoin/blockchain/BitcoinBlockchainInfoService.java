package network.arkane.provider.bitcoin.blockchain;

import network.arkane.provider.bitcoin.BitcoinEnv;
import network.arkane.provider.blockchain.BlockchainInfoService;
import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class BitcoinBlockchainInfoService implements BlockchainInfoService {

    private final BlockcypherGateway blockcypherGateway;
    private final BitcoinEnv bitcoinEnv;

    public BitcoinBlockchainInfoService(BlockcypherGateway blockcypherGateway,
                                        BitcoinEnv bitcoinEnv) {
        this.blockcypherGateway = blockcypherGateway;
        this.bitcoinEnv = bitcoinEnv;
    }

    @Override
    public SecretType type() {
        return SecretType.BITCOIN;
    }

    @Override
    public BigInteger getBlockNumber() {
        return blockcypherGateway.getBlockchain(bitcoinEnv.getNetwork())
                                 .getHeight();
    }

}
