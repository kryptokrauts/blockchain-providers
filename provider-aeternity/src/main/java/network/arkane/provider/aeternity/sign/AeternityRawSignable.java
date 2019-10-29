package network.arkane.provider.aeternity.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class AeternityRawSignable implements Signable {

  private String data;

  @Builder
  public AeternityRawSignable(String data) {
    this.data = data;
  }
}
