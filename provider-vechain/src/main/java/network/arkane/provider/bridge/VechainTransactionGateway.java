package network.arkane.provider.bridge;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.blockchain.TransferResult;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.gateway.VechainGateway;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.SubmittedAndSignedTransactionSignature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.token.TokenInfo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Service
@Slf4j
public class VechainTransactionGateway implements TransactionGateway {

    private VechainGateway vechainGateway;

    public VechainTransactionGateway(VechainGateway vechainGateway) {
        this.vechainGateway = vechainGateway;
    }

    @Override
    public SecretType getType() {
        return SecretType.VECHAIN;
    }

    @Override
    public Signature submit(final TransactionSignature signTransactionResponse) {
        try {
            TransferResult transferResult = vechainGateway.sendRawTransaction(signTransactionResponse.getSignedTransaction());
            return SubmittedAndSignedTransactionSignature.signAndSubmitTransactionBuilder()
                                                         .transactionHash(transferResult.getId())
                                                         .signedTransaction(signTransactionResponse.getSignedTransaction())
                                                         .build();
        } catch (final ArkaneException ex) {
            throw ex;
        } catch (final Exception ex) {
            log.error("Problem trying to send transaction to vechain");
            throw arkaneException()
                    .errorCode("transaction.submit.internal-error")
                    .message("problem trying to submit transaction to vechain: " + ex.getMessage())
                    .build();
        }
    }

    @Override
    public Optional<TokenInfo> getTokenInfo(final String tokenAddress) {
        final String name = vechainGateway.getTokenName(tokenAddress);
        final String symbol = vechainGateway.getTokenSymbol(tokenAddress);
        final BigInteger decimals = vechainGateway.getTokenDecimals(tokenAddress);

        if (name != null && decimals != null && symbol != null) {
            return Optional.of(TokenInfo.builder()
                                        .address(tokenAddress)
                                        .name(name)
                                        .decimals(decimals.intValue())
                                        .symbol(symbol)
                                        .type("VIP180")
                                        .build());
        } else {
            return Optional.empty();
        }
    }
}
