package network.arkane.provider.wallet.generation;

import network.arkane.provider.secret.generation.EvmSecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class EvmWalletGenerator implements WalletGenerator<EvmSecretKey> {

    @Override
    public GeneratedWallet generateWallet(final String password,
                                          final EvmSecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        try {
            final WalletFile theWallet = Wallet.createStandard(password, secret.getKeyPair());
            return GeneratedEvmWallet
                    .builder()
                    .secretType(secret.getType())
                    .walletFile(theWallet)
                    .address(getAddress(secret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final EvmSecretKey ethereumSecret) {
        return Keys.toChecksumAddress(Keys.getAddress(ethereumSecret.getKeyPair()));
    }
}
