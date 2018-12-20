package network.arkane.provider.wallet.generation;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.web3j.crypto.WalletFile;

@Builder
public class GeneratedAeternityWallet implements GeneratedWallet {

    private String address;

    public String getAddress() {
        return address;
    }

    public String secretAsBase64() {
        // TODO aeternity wallet to base64
        return null;
    }
}
