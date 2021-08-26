package network.arkane.provider.web3j;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.contract.DeltaBalances;
import network.arkane.provider.contract.ERC1155Token;
import network.arkane.provider.contract.HumanStandardToken;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gas.EvmEstimateGasResult;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Slf4j
public abstract class EvmWeb3jGateway {

    private static final BigInteger DEFAULT_GAS_LIMIT_FAILED = new BigInteger("500000");
    private Web3j web3j;
    private DeltaBalances deltaBalances;

    public abstract SecretType getSecretType();

    public EvmWeb3jGateway(final Web3j web3j,
                           final String deltaBalancesAddress) {
        this.web3j = web3j;
        deltaBalances = new DeltaBalances(deltaBalancesAddress, this.web3j);
    }

    public Web3j web3() {
        return web3j;
    }

    public EthGetBalance getBalance(final String account) {
        try {
            return web3().ethGetBalance(account, DefaultBlockParameterName.LATEST).send();
        } catch (final Exception ex) {
            log.error("Problem trying to get balance from the network");
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Unable to get the balance for the specified account (%s) ()", account))
                                 .build();
        }
    }

    public List<BigInteger> getTokenBalances(final String owner,
                                             final List<String> tokenAddress) {
        try {
            return deltaBalances.tokenBalances(owner, tokenAddress);
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the token balances of %s", owner), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the token balances of %s ()", owner))
                                 .build();
        }
    }

    public BigInteger getTokenBalance(final String owner,
                                      final String tokenAddress) {
        try {
            return getERC20(tokenAddress).balanceOf(owner).send();
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the token balance of %s for token %s", owner, tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the token balance of %s for token %s ()", owner, tokenAddress))
                                 .build();
        }
    }

    public BigInteger getApprovalBalance(final String owner,
                                         final String spender,
                                         final String tokenAddress) {
        try {
            return getERC20(tokenAddress).allowance(owner, spender).send();
        } catch (final Exception ex) {
            log.error(String.format("Problem trying to get the token balance of %s for token %s", owner, tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the token balance of %s for token %s ()", owner, tokenAddress))
                                 .build();
        }
    }

    public EvmEstimateGasResult estimateGas(final String from,
                                            final String to,
                                            BigInteger value,
                                            String data) {
        try {
            Transaction transaction = Transaction.createFunctionCallTransaction(
                    from,
                    BigInteger.ZERO, null, null, to, value, data);


            EthEstimateGas ethEstimateGas = web3().ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()) {
                return EvmEstimateGasResult.builder()
                                           .gasLimit(DEFAULT_GAS_LIMIT_FAILED)
                                           .reverted(true)
                                           .build();
            }
            BigInteger amountUsed = ethEstimateGas.getAmountUsed();
            return EvmEstimateGasResult.builder()
                                       .gasLimit(amountUsed)
                                       .reverted(false)
                                       .build();
        } catch (IOException e) {
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.estimate.gas.internal-error")
                                 .message("A problem occurred trying to estimate the gas.")
                                 .cause(e)
                                 .build();
        }
    }

    public BigInteger getNextNonce(String address) {
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            return ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.nonce.internal-error")
                                 .message("A problem occurred trying to get the next nonce")
                                 .cause(e)
                                 .build();
        }
    }

    public EthSendTransaction ethSendRawTransaction(final String signedTransaction) {
        try {
            return web3()
                    .ethSendRawTransaction(signedTransaction)
                    .send();
        } catch (final Exception ex) {
            log.error("Problem trying to submit transaction to the network: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.transaction.submit.internal-error")
                                 .message("A problem occurred trying to submit the transaction to the network")
                                 .cause(ex)
                                 .build();
        }
    }

    public EthGasPrice ethGasPrice() {
        try {
            return web3()
                    .ethGasPrice()
                    .send();
        } catch (final Exception ex) {
            log.error("Problem trying get eth gas price: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.eth.gasprice.internal-error")
                                 .message("Problem trying get eth gas price")
                                 .cause(ex)
                                 .build();
        }
    }

    public String getName(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).name().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the name for token %s ()", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the name for token %s ()", tokenAddress))
                                 .build();
        }
    }

    public String getSymbol(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).symbol().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the symbol for token %s ()", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the symbol for token %s ()", tokenAddress))
                                 .build();
        }
    }

    public BigInteger getDecimals(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).decimals().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the decimals for token %s ()", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the decimals for token %s ()", tokenAddress))
                                 .build();
        }
    }

    public List<BigInteger> getERC1155Balances(final String tokenAddress,
                                               final String userAddress,
                                               final List<BigInteger> tokenIds) {
        return getERC1155(tokenAddress).balanceOfBatch(userAddress, tokenIds);
    }

    private ERC1155Token getERC1155(final String tokenAddress) {
        return new ERC1155Token(tokenAddress, web3j);
    }

    private HumanStandardToken getERC20(final String token) {
        return HumanStandardToken.load(token, web3());
    }

}
