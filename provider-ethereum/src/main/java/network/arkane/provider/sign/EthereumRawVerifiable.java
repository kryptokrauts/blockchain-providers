package network.arkane.provider.sign;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.sign.domain.Verifiable;

@Data
@NoArgsConstructor
public class EthereumRawVerifiable implements Verifiable {
    private String signature;
    private String message;
    private String address;
    private boolean prefix = true;

    @Builder(builderClassName = "EthereumRawVerifiableBuilder")
    public EthereumRawVerifiable(String signature, String message, String address, boolean prefix) {
        this.signature = signature;
        this.message = message;
        this.address = address;
        this.prefix = prefix;
    }
}
