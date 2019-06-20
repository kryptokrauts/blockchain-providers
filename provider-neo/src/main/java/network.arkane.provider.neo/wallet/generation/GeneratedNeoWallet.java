package network.arkane.provider.neo.wallet.generation;

import lombok.Getter;
import lombok.Builder;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;
import io.neow3j.crypto.WalletFile;


@Builder
public class GeneratedNeoWallet implements GeneratedWallet {

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

