package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EvmEip712Signable implements Signable {
    private String data;

    @Builder(builderClassName = "EvmEip712SignableBuilder")
    public EvmEip712Signable(final String data) {
        this.data = data;
    }

}
