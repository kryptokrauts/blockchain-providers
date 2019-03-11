package network.arkane.provider.tron.wallet.extraction;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.tron.secret.generation.TronSecretKey;
import network.arkane.provider.tron.wallet.extraction.request.TronKeystoreExtractionRequest;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.springframework.stereotype.Component;
import org.tron.common.crypto.ECKey;
import org.tron.keystore.Wallet;
import org.tron.keystore.WalletFile;

@Component
public class TronKeystoreExtractor implements SecretExtractor<TronKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final TronKeystoreExtractionRequest importWalletRequest) {
        try {
            final WalletFile walletFile = JSONUtil.fromJson(importWalletRequest.getKeystore(), WalletFile.class);
            final ECKey keypair = Wallet.decrypt(importWalletRequest.getPassword(), walletFile);
            return TronSecretKey
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
