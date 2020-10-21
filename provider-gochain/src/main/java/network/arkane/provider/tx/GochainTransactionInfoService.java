package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class GochainTransactionInfoService extends EvmTransactionInfoService {


    public GochainTransactionInfoService(@Qualifier("gochainWeb3j") Web3j gochainWeb3j) {
        super(gochainWeb3j);
    }

    public SecretType type() {
        return SecretType.GOCHAIN;
    }

}
