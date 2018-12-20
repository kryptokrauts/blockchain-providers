package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.AeternitySecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AeternityWalletGenerator implements WalletGenerator<AeternitySecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password, final AeternitySecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        // TODO generate aeternity wallet
        return GeneratedAeternityWallet.builder().build();
    }

    @Override
    public Class<AeternitySecretKey> type() {
        return AeternitySecretKey.class;
    }
}
