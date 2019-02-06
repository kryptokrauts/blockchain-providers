package network.arkane.provider.litecoin.address;

import network.arkane.provider.litecoin.LitecoinEnv;
import org.bitcoinj.core.Address;
import org.springframework.stereotype.Component;

@Component
public class LitecoinP2SHConverter {

    private final LitecoinEnv litecoinEnv;

    public LitecoinP2SHConverter(LitecoinEnv litecoinEnv) {
        this.litecoinEnv = litecoinEnv;
    }


    public String convert(String input) {
        Address address = Address.fromBase58(litecoinEnv.getNetworkParameters(), input);

        if (address.getVersion() != 50) {
            return input;
        }

        byte[] hash160 = address.getHash160();


        return Address.fromP2SHHash(litecoinEnv.getNetworkParameters(), hash160).toBase58();
    }
}
