package network.arkane.provider.litecoin.wallet.generation;

import lombok.Builder;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;

@Builder
public class GeneratedLitecoinWallet implements GeneratedWallet {

    private String address;
    private LitecoinKeystore secret;

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String secretAsBase64() {
        return Base64.encodeBase64String(JSONUtil.toJson(secret).getBytes());
    }
}
