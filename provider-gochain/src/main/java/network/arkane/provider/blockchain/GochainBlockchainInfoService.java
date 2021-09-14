package network.arkane.provider.blockchain;

import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class GochainBlockchainInfoService extends EvmBlockchainInfoService {

    public GochainBlockchainInfoService(@Qualifier("gochainWeb3j") Web3j gochainWeb3j) {
        super(gochainWeb3j);
    }

    public SecretType type() {
        return SecretType.GOCHAIN;
    }

}
