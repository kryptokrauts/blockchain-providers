package network.arkane.provider.sign.domain;

import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;

class HexSignatureMother {
    public static HexSignature aHexSignature() {
        return HexSignature.builder()
                           .r(Hex.decode("9955af11969a2d2a7f860cb00e6a00cfa7c581f5df2dbe8ea16700b33f4b4b9b69f945012f7ea7d3febf11eb1b78e1adc2d1c14c2cf48b25000938cc1860c83e01".substring(
                                   0,
                                   64)))
                           .s(Hex.decode("9955af11969a2d2a7f860cb00e6a00cfa7c581f5df2dbe8ea16700b33f4b4b9b69f945012f7ea7d3febf11eb1b78e1adc2d1c14c2cf48b25000938cc1860c83e01".substring(
                                   64,
                                   128)))
                           .v(new BigInteger(Hex.decode(
                                   "9955af11969a2d2a7f860cb00e6a00cfa7c581f5df2dbe8ea16700b33f4b4b9b69f945012f7ea7d3febf11eb1b78e1adc2d1c14c2cf48b25000938cc1860c83e01".substring(
                                           128,
                                           130))).intValue())
                           .build();
    }
}