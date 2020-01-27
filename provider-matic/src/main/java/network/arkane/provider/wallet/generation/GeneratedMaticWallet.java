package network.arkane.provider.wallet.generation;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.JSONUtil;
import org.apache.commons.codec.binary.Base64;
import org.web3j.crypto.WalletFile;

@Builder
public class GeneratedMaticWallet implements GeneratedWallet {

    private String address;
    @Getter
    private WalletFile walletFile;

    public String getAddress() {
        return address;
    }

    public String secretAsBase64() {
        return Base64.encodeBase64String(JSONUtil.toJson(walletFile).getBytes());
    }
}
