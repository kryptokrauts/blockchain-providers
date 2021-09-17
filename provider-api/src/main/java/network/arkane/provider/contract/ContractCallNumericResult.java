package network.arkane.provider.contract;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCallNumericResult extends ContractCallResultType<BigInteger> {

    @Builder
    public ContractCallNumericResult(String type,
                                     BigInteger value) {
        super(type, value);
    }

}
