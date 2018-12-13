package network.arkane.provider.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.Prefix;
import network.arkane.provider.clients.AccountClient;
import network.arkane.provider.clients.DeltaBalancesContractClient;
import network.arkane.provider.clients.ERC20ContractClient;
import network.arkane.provider.clients.base.AbstractClient;
import network.arkane.provider.core.model.blockchain.Account;
import network.arkane.provider.core.model.blockchain.TransferRequest;
import network.arkane.provider.core.model.blockchain.TransferResult;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.ERC20Token;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.exceptions.ArkaneException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
@Slf4j
public class VechainGateway {

    private DeltaBalancesContractClient deltaBalancesContractClient;

    public VechainGateway(DeltaBalancesContractClient deltaBalancesContractClient) {
        this.deltaBalancesContractClient = deltaBalancesContractClient;
    }

    public Account getAccount(final String account) {
        try {
            return AccountClient.getAccountInfo(Address.fromHexString(account), Revision.BEST);
        } catch (final Exception ex) {
            log.error("Problem trying to get account from the Vechain network");
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Unable to get the properties for the specified account (%s) (vechain)", account))
                                 .build();
        }
    }

    public Amount getTokenBalance(final String owner, final ERC20Token token) {
        try {
            return ERC20ContractClient.getERC20Balance(Address.fromHexString(owner), token, Revision.BEST);
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the token balance of %s for token %s", owner, token.getContractAddress().toHexString(Prefix.ZeroLowerX)));
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Problem trying to get the token balance of %s for token %s (vechain)",
                                                        owner,
                                                        token.getContractAddress().toHexString(Prefix.ZeroLowerX)))
                                 .build();
        }
    }

    public List<BigInteger> getTokenBalances(final String owner, List<String> tokenAddresses) {
        try {
            return deltaBalancesContractClient.getVip180Balances(Address.fromHexString(owner), tokenAddresses);
        } catch (final Exception ex) {
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Problem trying to get the token balances of %s (vechain)", owner))
                                 .build();
        }
    }

    public String getTokenName(final String tokenAddress) {
        try {
            return ERC20ContractClient.name(Address.fromHexString(tokenAddress));
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the name for token %s (VeChain)", tokenAddress));
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Problem trying to get the name for token %s (VeChain)", tokenAddress))
                                 .build();
        }
    }

    public String getTokenSymbol(final String tokenAddress) {
        try {
            return ERC20ContractClient.symbol(Address.fromHexString(tokenAddress));
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the symbol for token %s (VeChain)", tokenAddress));
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Problem trying to get the symbol for token %s (VeChain)", tokenAddress))
                                 .build();
        }
    }

    public BigInteger getTokenDecimals(final String tokenAddress) {
        try {
            return ERC20ContractClient.decimals(Address.fromHexString(tokenAddress));
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the decimals for token %s (VeChain)", tokenAddress));
            throw ArkaneException.arkaneException()
                                 .errorCode("thorify.internal-error")
                                 .message(String.format("Problem trying to get the decimals for token %s (VeChain)", tokenAddress))
                                 .build();
        }
    }

    public TransferResult sendRawTransaction(final String signedTransaction) {
        TransferRequest request = new TransferRequest();
        request.setRaw(signedTransaction);
        try {
            return AbstractClient.sendPostRequest(AbstractClient.Path.PostTransaction, null, null, request, TransferResult.class);
        } catch (final Exception ex) {
            log.error("Problem trying to submit transaction to the Vechain network: {}", ex.getMessage());
            if (ex.getMessage() != null && ex.getMessage().contains("insufficient energy")) {
                throw ArkaneException.arkaneException()
                                     .errorCode("transaction.insufficient-funds")
                                     .message("The account that initiated the transfer does not have enough energy")
                                     .cause(ex)
                                     .build();
            } else {
                throw ArkaneException.arkaneException()
                                     .errorCode("thorify.transaction.submit.internal-error")
                                     .message("A problem occurred trying to submit the transaction to the Vechain network")
                                     .cause(ex)
                                     .build();
            }
        }
    }
}
