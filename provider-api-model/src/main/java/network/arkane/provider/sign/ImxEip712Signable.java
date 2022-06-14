package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Signable;

@Data
@NoArgsConstructor
public class ImxEip712Signable implements Signable {
    private String data;

    @Builder(builderClassName = "ImxEip712SignableBuilder")
    public ImxEip712Signable(final String data) {
        this.data = data;
    }

}
