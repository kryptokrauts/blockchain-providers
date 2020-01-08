package network.arkane.provider.tron.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Verifiable;

@Data
@NoArgsConstructor
public class TronRawVerifiable implements Verifiable {
    private String signature;
    private String message;
    private String address;

    @Builder(builderClassName = "TronRawVerifiableBuilder")
    public TronRawVerifiable(String signature, String message, String address) {
        this.signature = signature;
        this.message = message;
        this.address = address;
    }
}
