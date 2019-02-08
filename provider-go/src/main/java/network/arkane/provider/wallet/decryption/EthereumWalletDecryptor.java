package network.arkane.provider.wallet.decryption;

import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.wallet.generation.GeneratedEthereumWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;

@Component
public class EthereumWalletDecryptor implements WalletDecryptor<GeneratedEthereumWallet, EthereumSecretKey> {
    @Override
    public EthereumSecretKey generateKey(GeneratedEthereumWallet generatedWallet, String password) {
        try {
            final ECKeyPair keyPair = Wallet.decrypt(password, generatedWallet.getWalletFile());
            return EthereumSecretKey.builder().keyPair(keyPair).build();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
