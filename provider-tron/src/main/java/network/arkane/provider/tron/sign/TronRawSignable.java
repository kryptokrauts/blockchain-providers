package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class TronRawSignable implements Signable {
    private String data;

    @Builder
    public TronRawSignable(String data) {
        this.data = data;
    }
}
