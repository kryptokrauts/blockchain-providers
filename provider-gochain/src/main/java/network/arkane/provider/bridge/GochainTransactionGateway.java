package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

@Service
@Slf4j
public class GochainTransactionGateway extends EvmTransactionGateway {

    public GochainTransactionGateway(@Qualifier("gochainWeb3j") Web3j gochainWeb3j) {
        super(gochainWeb3j);
    }

    @Override
    public SecretType getType() {
        return SecretType.GOCHAIN;
    }

}
