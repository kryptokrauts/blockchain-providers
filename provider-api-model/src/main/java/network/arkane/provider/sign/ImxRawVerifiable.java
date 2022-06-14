package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Verifiable;

@Data
@NoArgsConstructor
public class ImxRawVerifiable implements Verifiable {
    private String signature;
    private String message;
    private String address;

    @Builder(builderClassName = "ImxRawVerifiableBuilder")
    public ImxRawVerifiable(String signature,
                            String message,
                            String address) {
        this.signature = signature;
        this.message = message;
        this.address = address;
    }
}
