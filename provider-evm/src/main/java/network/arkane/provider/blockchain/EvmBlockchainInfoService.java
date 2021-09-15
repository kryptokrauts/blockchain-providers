package network.arkane.provider.blockchain;

import network.arkane.provider.exceptions.ArkaneException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

public abstract class EvmBlockchainInfoService implements BlockchainInfoService {

    private final Web3j defaultWeb3j;

    protected EvmBlockchainInfoService(Web3j web3j) {
        this.defaultWeb3j = web3j;
    }

    @Override
    public BigInteger getBlockNumber() {
        try {
            Web3j web3J = getWeb3J(Collections.emptyMap());
            return web3J.ethBlockNumber()
                        .send()
                        .getBlockNumber();
        } catch (IOException e) {
            throw ArkaneException.arkaneException().message("Error getting block number").errorCode("blockchain.exception.unknown").build();
        }
    }

    private Web3j getWeb3J(Map<String, Object> parameters) {
        if (parameters != null && parameters.containsKey("endpoint")) {
            return Web3j.build(new HttpService((String) parameters.get("endpoint"), false));
        } else {
            return defaultWeb3j;
        }
    }
}
