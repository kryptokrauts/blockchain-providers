package network.arkane.provider.hedera.wallet;

import lombok.Builder;
import lombok.Getter;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import org.apache.commons.codec.binary.Base64;

public class GeneratedHederaWallet implements GeneratedWallet {

    private final String address;
    private final String keystore;

    @Builder
    public GeneratedHederaWallet(String address,
                                 String keystore) {
        this.address = address;
        this.keystore = keystore;
    }

    @Getter
    private SecretType secretType;

    public String getAddress() {
        return address;
    }

    public String secretAsBase64() {
        return Base64.encodeBase64String(keystore.getBytes());
    }
}
