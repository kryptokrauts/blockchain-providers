package network.arkane.provider.sign;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class AeternityContractCallSignable implements Signable {

  private BigInteger abiVersion;
  private String callData;
  private String contractId;
  private BigInteger gas;
  private BigInteger gasPrice;
  private BigInteger nonce;
  private String callerId;
  private BigInteger ttl;
  private BigInteger amount;
  private BigInteger fee;

  @Builder
  public AeternityContractCallSignable(BigInteger abiVersion, String callData, String contractId,
      BigInteger gas, BigInteger gasPrice, BigInteger nonce, String callerId, BigInteger ttl,
      BigInteger amount, BigInteger fee) {
    this.abiVersion = abiVersion;
    this.callData = callData;
    this.contractId = contractId;
    this.gas = gas;
    this.gasPrice = gasPrice;
    this.nonce = nonce;
    this.callerId = callerId;
    this.ttl = ttl;
    this.amount = amount;
    this.fee = fee;
  }
}
