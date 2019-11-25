package network.arkane.provider.sign.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;

@Data
@NoArgsConstructor
public class HexSignature extends Signature {

    private String r;
    private String s;
    private String v;
    private String signature;

    @Builder
    public HexSignature(final byte[] r,
                        final byte[] s,
                        final int v) {
        super(SignatureType.HEX_SIGNATURE);
        this.r = Hex.toHexString(r);
        this.s = Hex.toHexString(s);
        this.v = Integer.toHexString(v);
        this.signature = "0x" + this.r + this.s + this.v;
        this.r = "0x" + this.r;
        this.s = "0x" + this.s;
        this.v = "0x" + this.v;
    }
}
