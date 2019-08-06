package network.arkane.provider.neo.wallet.generation;

import io.neow3j.wallet.nep6.NEP6Wallet;
import lombok.Getter;
import lombok.Builder;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;


@Builder
public class GeneratedNeoWallet implements GeneratedWallet {

    private String address;
    @Getter
    private NEP6Wallet walletFile;

    public String getAddress() {
        return address;
    }

    public String secretAsBase64() {
        return Base64.encodeBase64String(JSONUtil.toJson(walletFile).getBytes());
    }
}

