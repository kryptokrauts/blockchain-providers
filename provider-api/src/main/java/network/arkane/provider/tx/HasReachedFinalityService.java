package network.arkane.provider.tx;

import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class HasReachedFinalityService {
    private final TransactionConfigurationProperties transactionConfigurationProperties;

    public HasReachedFinalityService(final TransactionConfigurationProperties transactionConfigurationProperties) {
        this.transactionConfigurationProperties = transactionConfigurationProperties;
    }

    public Boolean hasReachedFinality(final SecretType secretType,
                                       final BigInteger confirmations) {
        BigInteger confirmationNrForChain = transactionConfigurationProperties.getTxConfirmationNumber(secretType);
        return confirmationNrForChain != null && confirmations != null && confirmations.compareTo(confirmationNrForChain) >= 0;
    }
}
