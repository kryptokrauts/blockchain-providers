package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Service
@Slf4j
public class EthereumTransactionGateway implements TransactionGateway {

    private Web3j defaultWeb3j;

    public EthereumTransactionGateway(@Qualifier("ethereumWeb3j") Web3j web3j) {
        this.defaultWeb3j = web3j;
    }

    @Override
    public SecretType getType() {
        return SecretType.ETHEREUM;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse, final Optional<String> endpoint) {
        try {
            log.debug("Sending transaction to ethereum node {}", signTransactionResponse.getSignedTransaction());

            Web3j web3j = endpoint
                    .map(this::createWeb3j)
                    .orElse(defaultWeb3j);

            final EthSendTransaction send = ethSendRawTransaction(web3j, signTransactionResponse.getSignedTransaction());
            if (send.hasError()) {
                if (send.getError().getMessage().matches(".*[I,i]nsufficient funds.*")) {
                    log.warn("Got error from ethereum chain: insufficient funds");
                    throw arkaneException()
                            .errorCode("transaction.insufficient-funds")
                            .message("The account that initiated the transfer does not have enough energy")
                            .build();
                } else {
                    log.error("Got error from ethereum chain: {}", send.getError().getMessage());
                    throw arkaneException()
                            .errorCode("transaction.submit.ethereum-error")
                            .build();
                }
            } else {
                return signAndSubmitTransactionBuilder()
                        .transactionHash(send.getTransactionHash())
                        .signedTransaction(signTransactionResponse.getSignedTransaction())
                        .build();
            }
        } catch (final ArkaneException ex) {
            log.debug("Exception submitting transaction", ex);
            throw ex;
        } catch (final Exception ex) {
            log.error("Error trying to submit a signed transaction: {}", ex.getMessage());
            throw arkaneException()
                    .errorCode("transaction.submit.internal-error")
                    .message("A problem occurred trying to submit the transaction to the Ethereum network")
                    .cause(ex)
                    .build();
        }
    }

    private Web3j createWeb3j(final @Value("${network.arkane.ethereum.endpoint.url}") String endpoint) {
        return Web3j.build(new HttpService(endpoint, false));
    }

    private EthSendTransaction ethSendRawTransaction(final Web3j web3j, final String signedTransaction) {
        try {
            return web3j.ethSendRawTransaction(signedTransaction).send();
        } catch (final Exception ex) {
            log.error("Problem trying to submit transaction to the Ethereum network: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.transaction.submit.internal-error")
                                 .message("A problem occurred trying to submit the transaction to the Ethereum network")
                                 .cause(ex)
                                 .build();
        }
    }

}
