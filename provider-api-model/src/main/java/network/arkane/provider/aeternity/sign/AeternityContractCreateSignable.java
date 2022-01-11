package network.arkane.provider.aeternity.sign;

import com.kryptokrauts.aeternity.sdk.constants.BaseConstants;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AeternityContractCreateSignable implements Signable {

    private String contractByteCode;
    private String callData;
    private String ownerId;
    private BigInteger nonce;
    @Default private BigInteger amount = BigInteger.ZERO;
    @Default private BigInteger gasLimit = BaseConstants.CONTRACT_DEFAULT_GAS_LIMIT;
    @Default private BigInteger gasPrice = BaseConstants.MINIMAL_GAS_PRICE;
    @Default private BigInteger ttl = BigInteger.ZERO;
}
