package network.arkane.provider.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.contract.ERC1155Token;
import network.arkane.provider.contract.HumanStandardToken;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gas.MaticEstimateGasResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Slf4j
public class MaticWeb3JGateway {

    private static final BigInteger DEFAULT_GAS_LIMIT_FAILED = new BigInteger("200000");
    private final EnsResolver ensResolver;
    private final Web3j web3j;

    public MaticWeb3JGateway(final @Qualifier("maticWeb3j") Web3j maticWeb3j,
                             final String deltaBalancesAddress) {
        if (StringUtils.isEmpty(deltaBalancesAddress)) {
            throw new IllegalArgumentException("Address for deltabalances should be set [matic]");
        }
        this.web3j = maticWeb3j;
        ensResolver = new EnsResolver(this.web3j);
    }

    public Web3j web3() {
        return web3j;
    }

    public EthGetBalance getBalance(final String account) {
        try {
            return web3().ethGetBalance(account, DefaultBlockParameterName.LATEST).send();
        } catch (final Exception ex) {
            log.error("Problem trying to get balance from the Matic network", ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Unable to get the balance for the specified account (%s) (Matic)", account))
                                 .build();
        }
    }

    @Cacheable(value = "address_to_ens", key = "'matic-' + #address")
    public Optional<String> getEnsName(String address) {
        try {
            return Optional.ofNullable(ensResolver.reverseResolve(address));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Cacheable(value = "ens_to_address", key = "'matic' + #ensName")
    public Optional<String> getAddressForEnsName(String ensName) {
        try {
            return Optional.ofNullable(ensResolver.resolve(ensName));
        } catch (Exception e) {
            return Optional.empty();
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
                                 .message(String.format("Problem trying to get the token balance of %s for token %s (Matic)", owner, tokenAddress))
                                 .build();
        }
    }

    public MaticEstimateGasResult estimateGas(final String from,
                                              final String to,
                                              final BigInteger value,
                                              final String data) {
        try {
            BigInteger blockGasLimit = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getGasLimit();

            Transaction transaction = Transaction.createFunctionCallTransaction(
                    from,
                    BigInteger.ZERO, BigInteger.ONE, blockGasLimit, to, value, data);

            EthEstimateGas ethEstimateGas = web3().ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()) {
                return MaticEstimateGasResult.builder()
                                             .gasLimit(DEFAULT_GAS_LIMIT_FAILED)
                                             .reverted(true)
                                             .build();
            }
            BigInteger amountUsed = ethEstimateGas.getAmountUsed();
            return MaticEstimateGasResult.builder()
                                         .gasLimit(amountUsed)
                                         .reverted(amountUsed.compareTo(blockGasLimit) >= 0)
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
        EthGetTransactionCount ethGetTransactionCount;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(
                    address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Problem trying to get balance from the Matic network", e);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.nonce.internal-error")
                                 .message("A problem occurred trying to get the next nonce")
                                 .cause(e)
                                 .build();
        }
        return ethGetTransactionCount.getTransactionCount();

    }

    public String getName(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).name().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the name for token %s (Matic)", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the name for token %s (Matic)", tokenAddress))
                                 .build();
        }
    }

    public String getSymbol(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).symbol().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the symbol for token %s (Matic)", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the symbol for token %s (Matic)", tokenAddress))
                                 .build();
        }
    }

    public BigInteger getDecimals(final String tokenAddress) {
        try {
            return getERC20(tokenAddress).decimals().send();
        } catch (Exception ex) {
            log.error(String.format("Problem trying to get the decimals for token %s (Matic)", tokenAddress), ex);
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.internal-error")
                                 .message(String.format("Problem trying to get the decimals for token %s (Matic)", tokenAddress))
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
