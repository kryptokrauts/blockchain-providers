package network.arkane.blockchainproviders.covalent;

import network.arkane.blockchainproviders.covalent.dto.CovalentApiResponse;
import network.arkane.blockchainproviders.covalent.dto.CovalentChain;
import network.arkane.blockchainproviders.covalent.dto.CovalentTokenBalanceResponse;
import network.arkane.blockchainproviders.covalent.dto.CovalentTxHistoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class CovalentGateway {

    private final CovalentClient covalentClient;

    public CovalentGateway(@Value("${covalent.endpoint:http://nourl}") String endpoint,
                           @Value("${covalent.api-key:empty}") String apiKey) {
        this.covalentClient = new CovalentClient(endpoint, apiKey);
    }

    public CovalentTxHistoryResponse getTxHistory(final CovalentChain covalentChain,
                                                  final String walletAddress) {
        CovalentApiResponse<CovalentTxHistoryResponse> response = covalentClient.getTxHistory(covalentChain.getChainId(),
                                                                                              walletAddress);
        if (response.isError()) {
            throw new CovalentApiException(response.getErrorMessage());
        }
        return response.getData();
    }

    @Retryable(value = Exception.class,
               maxAttempts = 4,
               backoff = @Backoff(delay = 1000L, multiplier = 2))
    public CovalentTokenBalanceResponse getTokenBalances(String chainId,
                                                         String address) {
        return covalentClient.getTokenBalances(chainId, address);
    }

}
