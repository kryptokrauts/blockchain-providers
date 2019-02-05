package network.arkane.provider.wallet.extraction;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.EthereumKeystoreExtractionRequest;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class EthereumKeystoreExtractor implements SecretExtractor<EthereumKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final EthereumKeystoreExtractionRequest importWalletRequest) {
        try {
            final WalletFile walletFile = JSONUtil.fromJson(importWalletRequest.getKeystore(), WalletFile.class);
            final ECKeyPair keypair = Wallet.decrypt(importWalletRequest.getPassword(), walletFile);
            return EthereumSecretKey
                    .builder()
                    .keyPair(keypair)
                    .build();
        } catch (final Exception ex) {
            String msg = "Not a valid keystore file";
            if (ex.getMessage() != null && ex.getMessage().contains("Invalid password provided")) {
                msg = "Wrong password provided for given keystore file";
            }
            throw new IllegalArgumentException(msg);
        }
    }
}
