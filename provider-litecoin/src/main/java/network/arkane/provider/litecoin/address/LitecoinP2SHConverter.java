package network.arkane.provider.litecoin.address;

import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.litecoin.LitecoinEnv;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.WrongNetworkException;
import org.springframework.stereotype.Component;

@Component
public class LitecoinP2SHConverter {

    private final LitecoinEnv litecoinEnv;

    public LitecoinP2SHConverter(LitecoinEnv litecoinEnv) {
        this.litecoinEnv = litecoinEnv;
    }


    public String convert(String input) {
        Address address = toAddress(input);

        if (doNotConvertLAddresses(address)) {
            return input;
        }

        return Address.fromP2SHHash(
                litecoinEnv.getNetworkParameters(),
                address.getHash160()
        ).toBase58();
    }

    private boolean doNotConvertLAddresses(Address address) {
        return address.getVersion() != 50;
    }

    private Address toAddress(String input) {
        try {
            return Address.fromBase58(litecoinEnv.getNetworkParameters(), input);
        } catch (WrongNetworkException wrongNetworkException) {
            throw ArkaneException.arkaneException()
                    .errorCode("litecoin.address-wrong-network")
                    .message(wrongNetworkException.getMessage())
                    .build();
        }
    }
}
