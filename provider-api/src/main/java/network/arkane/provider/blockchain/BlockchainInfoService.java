package network.arkane.provider.blockchain;

import network.arkane.provider.chain.SecretType;

import java.math.BigInteger;

public interface BlockchainInfoService {

    SecretType type();

    BigInteger getBlockNumber();

}
