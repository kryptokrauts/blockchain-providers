package network.arkane.provider.litecoin;

import lombok.Value;
import network.arkane.provider.blockcypher.Network;
import org.bitcoinj.core.NetworkParameters;

@Value
public class LitecoinEnv {
    private Network network;
    private NetworkParameters networkParameters;
}
