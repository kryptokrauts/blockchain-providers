package network.arkane.provider.sochain;

import network.arkane.provider.sochain.domain.BalanceResult;
import network.arkane.provider.sochain.domain.Network;
import network.arkane.provider.sochain.domain.SendSignedTransactionRequest;
import network.arkane.provider.sochain.domain.SendSignedTransactionResult;
import network.arkane.provider.sochain.domain.SoChainResult;
import network.arkane.provider.sochain.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoChainGateway {

    private static final String USER_AGENT = "curl/7.54.0";

    private SoChainClient soChainClient;

    public SoChainGateway(SoChainClient soChainClient) {
        this.soChainClient = soChainClient;
    }

    public BalanceResult getBalance(Network network, String address) {
        return parseResult(soChainClient.getBalance(USER_AGENT, network.name(), address));
    }

    public List<Transaction> getUnspentTransactions(Network network, String address) {
        return parseResult(soChainClient.getUnspentTransactions(USER_AGENT, network.name(), address)).getTransactions();
    }

    public List<Transaction> getReceivedTransactions(Network network, String address) {
        return parseResult(soChainClient.getReceivedTransactions(USER_AGENT, network.name(), address)).getTransactions();
    }

    public List<Transaction> getSpentTransactions(Network network, String address) {
        return parseResult(soChainClient.getSpentTransactions(USER_AGENT, network.name(), address)).getTransactions();
    }

    public SendSignedTransactionResult sendSignedTransaction(Network network, String signedTransactionAsHex) {
        return parseResult(soChainClient.sendSignedTransaction(USER_AGENT, network.name(), new SendSignedTransactionRequest(signedTransactionAsHex)));
    }

    private <T> T parseResult(SoChainResult<T> result) {
        if (result.getStatus().equalsIgnoreCase("fail")) {
            throw new RuntimeException("Error when retrieving data from chain");
        } else {
            return result.getData();
        }
    }
}
