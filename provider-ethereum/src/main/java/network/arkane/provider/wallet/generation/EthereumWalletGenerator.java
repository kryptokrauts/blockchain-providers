package network.arkane.provider.wallet.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class EthereumWalletGenerator implements AbstractWalletGenerator {

    @Override
    public GeneratedWallet generateWallet(final String password, final SecretKey secret) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password should not be empty");
        }

        final EthereumSecretKey ethereumSecret = (EthereumSecretKey) secret;

        try {
            final WalletFile theWallet = Wallet.createStandard(password, ((EthereumSecretKey) secret).getKeyPair());
            return GeneratedEthereumWallet
                    .builder()
                    .walletFile(theWallet)
                    .address(getAddress(ethereumSecret))
                    .build();
        } catch (CipherException e) {
            throw new IllegalArgumentException("Unable to generate a wallet from the provided keypair");
        }
    }

    private String getAddress(final EthereumSecretKey ethereumSecret) {
        return Keys.toChecksumAddress(Keys.getAddress(ethereumSecret.getKeyPair()));
    }

    @Override
    public SecretType type() {
        return SecretType.ETHEREUM;
    }
}
