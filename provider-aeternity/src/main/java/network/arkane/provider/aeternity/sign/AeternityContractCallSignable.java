package network.arkane.provider.aeternity.sign;

import com.kryptokrauts.aeternity.sdk.constants.VirtualMachine;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class AeternityContractCallSignable implements Signable {

    private String callData;
    private String contractId;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger nonce;
    private String callerId;
    private BigInteger ttl;
    private BigInteger amount;
    private BigInteger fee;
    private VirtualMachine targetVM;

    @Builder
    public AeternityContractCallSignable(String callData, String contractId,
                                         BigInteger gas, BigInteger gasPrice, BigInteger nonce, String callerId, BigInteger ttl,
                                         BigInteger amount, BigInteger fee, VirtualMachine targetVM) {
        this.callData = callData;
        this.contractId = contractId;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.nonce = nonce;
        this.callerId = callerId;
        this.ttl = ttl;
        this.amount = amount;
        this.fee = fee;
        this.targetVM = targetVM;
    }
}
