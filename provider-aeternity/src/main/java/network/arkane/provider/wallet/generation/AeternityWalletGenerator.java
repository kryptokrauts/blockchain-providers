package network.arkane.provider.wallet.generation;

import com.kryptokrauts.aeternity.sdk.constants.ApiIdentifiers;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletService;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletServiceFactory;
import com.kryptokrauts.aeternity.sdk.util.EncodingUtils;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AeternityWalletGenerator implements WalletGenerator<AeternitySecretKey> {

    private final WalletService walletService = new WalletServiceFactory().getService();

    @Override
    public GeneratedWallet generateWallet(final String password, final AeternitySecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }
        try {
            final String keystoreJson = walletService.generateKeystore(secret.getKeyPair(), password, null);
            return GeneratedAeternityWallet
                    .builder()
                    .keystoreJson(keystoreJson)
                    .address(getAddress(secret))
                    .build();
        } catch (AException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    @Override
    public Class<AeternitySecretKey> type() {
        return AeternitySecretKey.class;
    }

    private String getAddress(final AeternitySecretKey aeternitySecretKey) {
        return EncodingUtils.encodeCheck(aeternitySecretKey.getKeyPair().getPublicKey(), ApiIdentifiers.ACCOUNT_PUBKEY);
    }
}
