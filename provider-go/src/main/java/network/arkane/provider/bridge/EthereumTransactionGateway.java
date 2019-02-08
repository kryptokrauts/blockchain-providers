package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.Web3JGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;
import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;
import static network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder;

@Service
@Slf4j
public class EthereumTransactionGateway implements TransactionGateway {

    private Web3JGateway web3j;

    public EthereumTransactionGateway(Web3JGateway web3j) {
        this.web3j = web3j;
    }

    @Override
    public SecretType getType() {
        return SecretType.ETHEREUM;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse) {
        try {
            log.debug("Sending transaction to ethereum node {}", signTransactionResponse.getSignedTransaction());
            final EthSendTransaction send = web3j.ethSendRawTransaction(signTransactionResponse.getSignedTransaction());
            if (send.hasError()) {
                if (send.getError().getMessage().contains("Insufficient funds")) {
                    log.debug("Got error from ethereum chain: insufficient funds");
                    throw arkaneException()
                            .errorCode("transaction.insufficient-funds")
                            .message("The account that initiated the transfer does not have enough energy")
                            .build();
                } else {
                    log.debug("Got error from ethereum chain: {}", send.getError().getMessage());
                    throw arkaneException()
                            .errorCode("transaction.submit.ethereum-error")
                            .message(send.getError().getMessage())
                            .build();
                }
            } else {
                log.debug("Updating last nonce");
                //TODO: update last used nonce (using events)
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
}
