package network.arkane.provider.aeternity.wallet.generation;

import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreService;
import com.kryptokrauts.aeternity.sdk.service.keystore.KeystoreServiceFactory;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.generation.GeneratedWallet;
import network.arkane.provider.wallet.generation.WalletGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AeternityWalletGenerator implements WalletGenerator<AeternitySecretKey> {

    private final KeystoreService keystoreService = new KeystoreServiceFactory().getService();

    @Override
    public GeneratedWallet generateWallet(final String password, final AeternitySecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }
        try {
            final String keystoreJson = keystoreService.createKeystore(secret.getKeyPair(), password, null);
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
        return aeternitySecretKey.getKeyPair().getAddress();
    }
}
