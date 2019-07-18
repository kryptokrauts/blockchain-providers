package network.arkane.provider.sign;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class AeternityContractCreateSignable implements Signable {

  private BigInteger abiVersion;
  private BigInteger amount;
  private String callData;
  private String contractByteCode;
  private BigInteger deposit;
  private BigInteger gas;
  private BigInteger gasPrice;
  private BigInteger nonce;
  private String ownerId;
  private BigInteger ttl;
  private BigInteger vmVersion;
  private BigInteger fee;

  @Builder
  public AeternityContractCreateSignable(BigInteger abiVersion, BigInteger amount, String callData,
      String contractByteCode, BigInteger deposit, BigInteger gas, BigInteger gasPrice,
      BigInteger nonce, String ownerId, BigInteger ttl, BigInteger vmVersion, BigInteger fee) {
    this.abiVersion = abiVersion;
    this.amount = amount;
    this.callData = callData;
    this.contractByteCode = contractByteCode;
    this.deposit = deposit;
    this.gas = gas;
    this.gasPrice = gasPrice;
    this.nonce = nonce;
    this.ownerId = ownerId;
    this.ttl = ttl;
    this.vmVersion = vmVersion;
    this.fee = fee;
  }
}
