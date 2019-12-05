package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.MaticSecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class MaticWalletGenerator implements WalletGenerator<MaticSecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password,
                                          final MaticSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        try {
            final WalletFile theWallet = Wallet.createStandard(password, secret.getKeyPair());
            return GeneratedMaticWallet
                    .builder()
                    .walletFile(theWallet)
                    .address(getAddress(secret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final MaticSecretKey maticSecret) {
        return Keys.toChecksumAddress(Keys.getAddress(maticSecret.getKeyPair()));
    }
}
