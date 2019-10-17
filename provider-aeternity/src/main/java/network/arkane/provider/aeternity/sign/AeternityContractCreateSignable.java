package network.arkane.provider.aeternity.sign;

import com.kryptokrauts.aeternity.sdk.constants.VirtualMachine;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class AeternityContractCreateSignable implements Signable {

    private BigInteger amount;
    private String callData;
    private String contractByteCode;
    private BigInteger deposit;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger nonce;
    private String ownerId;
    private BigInteger ttl;
    private BigInteger fee;
    private VirtualMachine targetVM;

    @Builder
    public AeternityContractCreateSignable(BigInteger amount, String callData,
                                           String contractByteCode, BigInteger deposit, BigInteger gas, BigInteger gasPrice,
                                           BigInteger nonce, String ownerId, BigInteger ttl, BigInteger fee, VirtualMachine targetVM) {
        this.amount = amount;
        this.callData = callData;
        this.contractByteCode = contractByteCode;
        this.deposit = deposit;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.nonce = nonce;
        this.ownerId = ownerId;
        this.ttl = ttl;
        this.fee = fee;
        this.targetVM = targetVM;
    }
}
