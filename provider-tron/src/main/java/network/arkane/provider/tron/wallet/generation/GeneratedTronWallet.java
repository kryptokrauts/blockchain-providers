package network.arkane.provider.tron.wallet.generation;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;
import org.tron.keystore.WalletFile;

@Builder
public class GeneratedTronWallet implements GeneratedWallet {

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
