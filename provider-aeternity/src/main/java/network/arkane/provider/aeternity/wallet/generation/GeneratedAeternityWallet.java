package network.arkane.provider.aeternity.wallet.generation;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;

@Builder
public class GeneratedAeternityWallet implements GeneratedWallet {

    private String address;

    @Getter
    private String keystoreJson;

    public String getAddress() {
        return address;
    }

    public String secretAsBase64() {
        return Base64.encodeBase64String(keystoreJson.getBytes());
    }
}
