package network.arkane.provider.blockchain;

import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class EthereumBlockchainInfoService extends EvmBlockchainInfoService {

    public EthereumBlockchainInfoService(@Qualifier("ethereumWeb3j") Web3j web3j) {
        super(web3j);
    }

    public SecretType type() {
        return SecretType.ETHEREUM;
    }

}
