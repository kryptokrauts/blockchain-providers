package network.arkane.provider.neo.wallet.extraction;

import network.arkane.provider.JSONUtil;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.springframework.stereotype.Component;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.Wallet;
import io.neow3j.crypto.WalletFile;

@Component
public class NeoKeystoreExtractor implements SecretExtractor<NeoKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final NeoKeystoreExtractionRequest importWalletRequest) {
        try {
            final WalletFile walletFile = JSONUtil.fromJson(importWalletRequest.getKeystore(), WalletFile.class);
            final WalletFile.Account account = walletFile.getAccounts().get(0);

            final ECKeyPair keyPair = Wallet.decryptStandard(importWalletRequest.getPassword(), walletFile, account);
            return NeoSecretKey
                    .builder()
                    .key(keyPair)
                    .build();
        } catch (final Exception ex) {
            String msg = "Not a valid keystore file or Invalid password";
            throw new IllegalArgumentException(msg);
        }
    }
}