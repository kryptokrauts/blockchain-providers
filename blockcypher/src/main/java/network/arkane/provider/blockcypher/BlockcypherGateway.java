package network.arkane.provider.blockcypher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionRequest;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlockcypherGateway {

    private static final String USER_AGENT = "curl/7.54.0";
    private final ObjectMapper objectMapper;

    private BlockcypherClient blockcypherClient;
    private String token;


    @Autowired
    public BlockcypherGateway(BlockcypherClient blockcypherClient, @Value("${blockcypher.token}") String token) {
        this.blockcypherClient = blockcypherClient;
        this.token = token;
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    public BlockcypherAddress getBalance(Network network, String address) {
        return objectMapper.readValue(
                blockcypherClient.getBalance(USER_AGENT, network.getCoin(), network.getChain(), token, address),
                BlockcypherAddress.class);
    }

    @SneakyThrows
    public BlockcypherAddressUnspents getUnspentTransactions(Network network, String address) {
        return objectMapper.readValue(
                blockcypherClient.getUnspents(USER_AGENT, network.getCoin(), network.getChain(), token, address),
                BlockcypherAddressUnspents.class);
    }

    @SneakyThrows
    public BlockCypherRawTransactionResponse sendSignedTransaction(Network network, String txAsHex) {
        return objectMapper.readValue(
                blockcypherClient.sendSignedTransaction(USER_AGENT, network.getCoin(), network.getChain(), token, new BlockCypherRawTransactionRequest(txAsHex)),
                BlockCypherRawTransactionResponse.class);
    }
}
