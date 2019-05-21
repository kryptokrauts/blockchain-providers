package network.arkane.provider.neo.gateway;

import io.neow3j.model.types.ContractParameter;
import io.neow3j.model.types.ContractParameterType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.NeoGetAccountState;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.utils.Numeric;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.token.TokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
@Primary
public class NeoW3JGateway {

    private Neow3j neow3j;

    public NeoW3JGateway(@Qualifier("neow3j") Neow3j NeoW3j,
                                final @Value("${network.arkane.ethereum.deltabalances.contract-address}") String deltaBalancesAddress) {
        if (StringUtils.isEmpty(deltaBalancesAddress)) {
            throw new IllegalArgumentException("address for deltabalances should be set [neo]");
        }
        this.neow3j = NeoW3j;
    }

    public Neow3j web3() {
        return neow3j;
    }

    public List<NeoGetAccountState.Balance> getBalance(final String account) {
        try {
            final List<NeoGetAccountState.Balance> balances = web3().getAccountState(account).send().getAccountState().getBalances();
            return balances;
        } catch (final Exception ex) {
            log.error("Problem trying to get balance from the Neo network");
            throw ArkaneException.arkaneException()
                    .errorCode("neow3j.internal-error")
                    .message(String.format("Unable to get the balance for the specified account (%s) (Neo)", account))
                    .build();
        }
    }

    public List<BigInteger> getTokenBalances(final String owner, final List<String> tokenAddress) {
        throw new UnsupportedOperationException("Not implemented yet for neo");
    }

    public BigInteger getTokenBalance(final String owner, final String tokenAddress) {
        try {

            final BigInteger tokenBalance= Numeric.toBigInt(web3().invokeFunction(tokenAddress,"balanceOf",
                    Arrays.asList(new ContractParameter(ContractParameterType.HASH160, owner)) ).send()
                    .getResult().getStack().get(0).getValue().toString());
            return tokenBalance;

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

