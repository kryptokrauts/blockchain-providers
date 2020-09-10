package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class EthereumEip712Signable implements Signable {
    private String data;

    @Builder(builderClassName = "EthereumEip712SignableBuilder")
    public EthereumEip712Signable(final String data) {
        this.data = data;
    }

}
