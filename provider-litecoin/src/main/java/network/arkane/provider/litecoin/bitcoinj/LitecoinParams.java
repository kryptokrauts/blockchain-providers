package network.arkane.provider.litecoin.bitcoinj;

import org.bitcoinj.params.AbstractBitcoinNetParams;

public class LitecoinParams extends AbstractBitcoinNetParams {

    /*
    48 --> L.. addresses
    50 --> the new M.. addresses
    Blockcypher doesn't support M address at the moment
     */
    private final int prefix = 48;

    @Override
    public String getPaymentProtocolId() {
        return "main";
    }

    @Override
    public int getAddressHeader() {
        return prefix;
    }

    @Override
    public int getP2SHHeader() {
        return prefix;
    }

}
