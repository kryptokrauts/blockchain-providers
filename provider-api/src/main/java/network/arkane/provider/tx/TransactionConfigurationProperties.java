package network.arkane.provider.tx;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import network.arkane.provider.chain.SecretType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.Map;

@Configuration
@ConfigurationProperties("io.venly.transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionConfigurationProperties {

    @Setter
    private Map<SecretType, BigInteger> chainSpecificConfirmationNumbers;

    public BigInteger getTxConfirmationNumber(SecretType secretType) {
        return chainSpecificConfirmationNumbers.get(secretType);
    }
}
