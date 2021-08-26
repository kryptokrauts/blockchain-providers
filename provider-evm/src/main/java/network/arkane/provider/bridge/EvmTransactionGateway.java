package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Slf4j
public abstract class EvmTransactionGateway implements TransactionGateway {

    private Web3j defaultWeb3j;

    public EvmTransactionGateway(Web3j web3j) {
        this.defaultWeb3j = web3j;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse,
                            final Optional<String> endpoint) {
        try {
            log.debug("Sending transaction to " + getType().name() + " node {}", signTransactionResponse.getSignedTransaction());

            Web3j web3j = endpoint
                    .map(this::createWeb3j)
                    .orElse(defaultWeb3j);

            final EthSendTransaction send = ethSendRawTransaction(web3j, signTransactionResponse.getSignedTransaction());
            if (send.hasError()) {
                if (send.getError().getMessage().matches(".*[I,i]nsufficient funds.*")) {
                    log.warn("Got error from " + getType().name() + " chain: insufficient funds");
                    throw arkaneException()
                            .errorCode("transaction.insufficient-funds")
                            .message(send.getError().getMessage())
                            .build();
                } else {
                    log.error("Got error from " + getType().name() + " chain: {}", send.getError().getMessage());
                    throw arkaneException()
                            .errorCode("transaction.submit." + getType().name().toLowerCase() + "-error")
                            .message(send.getError().getMessage())
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
                    .message("A problem occurred trying to submit the transaction to the " + getType().name() + " network")
                    .cause(ex)
                    .build();
        }
    }

    private Web3j createWeb3j(String endpoint) {
        return Web3j.build(new HttpService(endpoint, false));
    }

    private EthSendTransaction ethSendRawTransaction(final Web3j web3j,
                                                     final String signedTransaction) {
        try {
            return web3j.ethSendRawTransaction(signedTransaction).send();
        } catch (final Exception ex) {
            log.error("Problem trying to submit transaction to the " + getType().name() + " network: {}", ex.getMessage());
            throw ArkaneException.arkaneException()
                                 .errorCode("web3j.transaction.submit.internal-error")
                                 .message("A problem occurred trying to submit the transaction to the " + getType().name() + " network")
                                 .cause(ex)
                                 .build();
        }
    }
}
