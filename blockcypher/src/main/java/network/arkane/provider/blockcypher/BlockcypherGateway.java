package network.arkane.provider.blockcypher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionRequest;
import network.arkane.provider.blockcypher.domain.BlockCypherRawTransactionResponse;
import network.arkane.provider.blockcypher.domain.BlockcypherAddress;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class BlockcypherGateway {

    private static final String USER_AGENT = "curl/7.54.0";
    private final ObjectMapper objectMapper;

    private BlockcypherClient blockcypherClient;
    private String token;
    private RateLimiter rateLimiter;


    @Autowired
    public BlockcypherGateway(BlockcypherClient blockcypherClient,
                              @Value("${blockcypher.token}") String token,
                              @Value("${blockcypher.maxrequestspersecond:3}") Long maxRequestsPerSecond) {
        this.blockcypherClient = blockcypherClient;
        this.token = token;
        this.objectMapper = new ObjectMapper();
        this.rateLimiter = RateLimiter.create(maxRequestsPerSecond);
    }

    @SneakyThrows
    @Cacheable(value = "blockcypherBalance")
    public BlockcypherAddress getBalance(Network network, String address) {
        return objectMapper.readValue(
                executeWithRateLimiter(() -> blockcypherClient.getBalance(USER_AGENT, network.getCoin(), network.getChain(), token, address)),
                BlockcypherAddress.class);
    }

    @SneakyThrows
    @Cacheable(value = "blockcypherUnspents")
    public BlockcypherAddressUnspents getUnspentTransactions(Network network, String address) {
        return objectMapper.readValue(
                executeWithRateLimiter(() -> blockcypherClient.getUnspents(USER_AGENT, network.getCoin(), network.getChain(), token, address)),
                BlockcypherAddressUnspents.class);
    }

    @SneakyThrows
    public BlockCypherRawTransactionResponse sendSignedTransaction(Network network, String txAsHex) {
        return objectMapper.readValue(
                executeWithRateLimiter(() -> blockcypherClient.sendSignedTransaction(USER_AGENT,
                                                                                     network.getCoin(),
                                                                                     network.getChain(),
                                                                                     token,
                                                                                     new BlockCypherRawTransactionRequest(txAsHex))),
                BlockCypherRawTransactionResponse.class);
    }

    private <T> T executeWithRateLimiter(Callable<T> callable) {
        if (rateLimiter.tryAcquire(30, TimeUnit.SECONDS)) {
            try {
                return callable.call();
            } catch (Exception e) {
                log.debug("Exception trying to call blockcypher", e);
                throw new RuntimeException(e);
            }
        } else {
            log.debug("Unable to fetch for blockcypher");
            throw new RuntimeException("Bitcoin endpoint too busy");
        }
    }
}
