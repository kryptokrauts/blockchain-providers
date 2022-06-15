package io.venly.provider.imx.extraction;

import io.venly.provider.imx.extraction.request.ImxKeystoreExtractionRequest;
import io.venly.provider.imx.secret.ImxSecretKey;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.SecretExtractor;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

@Component
public class ImxKeystoreExtractor implements SecretExtractor<ImxKeystoreExtractionRequest> {

    @Override
    public SecretKey extract(final ImxKeystoreExtractionRequest importWalletRequest) {
        try {
            final WalletFile walletFile = JSONUtil.fromJson(importWalletRequest.getKeystore(), WalletFile.class);
            if (walletFile.getCrypto().getKdfparams() instanceof WalletFile.ScryptKdfParams) {
                WalletFile.ScryptKdfParams kdfparams = (WalletFile.ScryptKdfParams) walletFile.getCrypto().getKdfparams();
                checkKdfParams(kdfparams);
            }
            final ECKeyPair keypair = Wallet.decrypt(importWalletRequest.getPassword(), walletFile);
            return ImxSecretKey
                    .builder()
                    .type(importWalletRequest.getSecretType())
                    .keyPair(keypair)
                    .build();
        } catch (final Exception ex) {
            String msg = "Not a valid keystore file: " + ex.getMessage();
            if (ex.getMessage() != null && ex.getMessage().contains("Invalid password provided")) {
                msg = "Wrong password provided for given keystore file";
            }
            throw new IllegalArgumentException(msg);
        }
    }

    private void checkKdfParams(WalletFile.ScryptKdfParams kdfparams) {
        if (kdfparams.getR() > 10) {
            throw new RuntimeException("Invalid keystore file: lower R parameter");
        }
        if (kdfparams.getP() > 1) {
            throw new RuntimeException("Invalid keystore file: lower P parameter");
        }
        if (kdfparams.getN() > 524288) {
            throw new RuntimeException("Invalid keystore file: lower N parameter");
        }
    }

}
