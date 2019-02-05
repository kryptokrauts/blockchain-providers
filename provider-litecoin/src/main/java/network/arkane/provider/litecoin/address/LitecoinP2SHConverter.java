package network.arkane.provider.litecoin.address;

import network.arkane.provider.litecoin.bitcoinj.LitecoinParams;
import org.bitcoinj.core.Address;
import org.springframework.stereotype.Component;

@Component
public class LitecoinP2SHConverter {


    public String convert(String input) {
        Address address = Address.fromBase58(new LitecoinParams(), input);

        if (address.getVersion() != 50) {
            return input;
        }

        byte[] hash160 = address.getHash160();


        return Address.fromP2SHHash(new LitecoinParams(), hash160).toBase58();
    }
}
