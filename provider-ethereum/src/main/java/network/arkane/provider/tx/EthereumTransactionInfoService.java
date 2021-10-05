package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class EthereumTransactionInfoService extends EvmTransactionInfoService {

    public EthereumTransactionInfoService(@Qualifier("ethereumWeb3j") final Web3j web3j,
                                          final HasReachedFinalityService hasReachedFinalityService) {
        super(web3j, hasReachedFinalityService);
    }

    public SecretType type() {
        return SecretType.ETHEREUM;
    }


}
