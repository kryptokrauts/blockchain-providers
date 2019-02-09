package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.GochainSecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class GochainWalletGenerator implements WalletGenerator<GochainSecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password, final GochainSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        try {
            final WalletFile theWallet = Wallet.createStandard(password, ((GochainSecretKey) secret).getKeyPair());
            return GeneratedGochainWallet
                    .builder()
                    .walletFile(theWallet)
                    .address(getAddress(secret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final GochainSecretKey gochainSecret) {
        return Keys.toChecksumAddress(Keys.getAddress(gochainSecret.getKeyPair()));
    }

    @Override
    public Class<GochainSecretKey> type() {
        return GochainSecretKey.class;
    }
}
