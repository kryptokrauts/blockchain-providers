package network.arkane.provider.sign.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RawSignature extends Signature {
    private String signature;

    public RawSignature() {
        super(SignatureType.RAW_SIGNATURE);
    }

    @Builder
    public RawSignature(String signature) {
        super(SignatureType.RAW_SIGNATURE);
        this.signature = signature;
    }
}
