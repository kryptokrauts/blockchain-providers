package network.arkane.provider.neo.wallet.extraction;

import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import io.neow3j.wallet.nep6.NEP6Wallet;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.neo.NeoW3JConfiguration;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.springframework.stereotype.Component;

import io.neow3j.crypto.ECKeyPair;

@Component
public class NeoKeystoreExtractor implements SecretExtractor<NeoKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final NeoKeystoreExtractionRequest importWalletRequest) {
        try {
            NEP6Wallet nep6Wallet = JSONUtil.fromJson(importWalletRequest.getKeystore(), NEP6Wallet.class);
            Wallet wallet= Wallet.fromNEP6Wallet(nep6Wallet).build();
            Account  account= wallet.getAccounts().get(0);
            account.decryptPrivateKey(importWalletRequest.getPassword(),NeoW3JConfiguration.defaultScrypt);

            final ECKeyPair keyPair = account.getECKeyPair();
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