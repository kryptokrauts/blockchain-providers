package network.arkane.provider.neo.gateway;

import io.neow3j.model.types.ContractParameter;
import io.neow3j.model.types.ContractParameterType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
@Primary
public class NeoW3JGateway {

    private Neow3j neow3j;

    public NeoW3JGateway(@Qualifier("neow3j") Neow3j NeoW3j) {
        this.neow3j = NeoW3j;
    }

    public Neow3j web3() {
        return neow3j;
    }

    public List<NeoGetAccountState.Balance> getBalance(final String account) {
        try {
            return web3().getAccountState(account).send().getAccountState().getBalances();
        } catch (final Exception ex) {
            log.error("Problem trying to get balance from the Neo network");
            throw ArkaneException.arkaneException()
                    .errorCode("neow3j.internal-error")
                    .message(String.format("Unable to get the balance for the specified account (%s) (Neo)", account))
                    .build();
        }
    }

    // will change to new interface getnep5balances in neo 2.10.1 after neow3j update
    public List<BigInteger> getTokenBalances(final String owner, final List<String> tokenAddress) {
        return tokenAddress.stream().map(p -> getTokenBalance(owner, p)).collect(Collectors.toList());
    }

    public BigInteger getTokenBalance(final String owner, final String tokenAddress) {
        try {

            return  Numeric.toBigInt(web3().invokeFunction(tokenAddress, "balanceOf",
                    Arrays.asList(new ContractParameter(ContractParameterType.HASH160, owner)))
                    .send()
                    .getResult()
                    .getStack()
                    .get(0)
                    .getValue()
                    .toString());
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the token balance of %s for token %s", owner, tokenAddress), ex);
            throw ArkaneException.arkaneException()
                    .errorCode("neow3j.internal-error")
                    .message(String.format("Problem trying to get the token balance of %s for token %s (Neo)", owner, tokenAddress))
                    .build();
        }
    }

    public NeoSendRawTransaction sendRawTransaction(final String signedTransaction) {
        try {
            return web3()
                    .sendRawTransaction(signedTransaction)
                    .send();
        } catch (final Exception ex) {
            log.error("Problem trying to submit transaction to the Neo network: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                    .errorCode("neow3j.transaction.submit.internal-error")
                    .message("A problem occurred trying to submit the transaction to the Neo network")
                    .cause(ex)
                    .build();
        }
    }

}

