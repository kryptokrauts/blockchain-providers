package network.arkane.provider.litecoin.bitcoinj;

import org.bitcoinj.params.AbstractBitcoinNetParams;

public class LitecoinParams extends AbstractBitcoinNetParams {

    /*
    48 --> L.. addresses
    50 --> the new M.. addresses
    Blockcypher doesn't support M address at the moment
     */

    public LitecoinParams() {
        super();
        p2shHeader = 5;
        dumpedPrivateKeyHeader = 176;
        addressHeader = 48;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader, 50};
        id = "org.litecoin.production";
    }


    @Override
    public String getPaymentProtocolId() {
        return "main";
    }


}
