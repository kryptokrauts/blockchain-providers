package network.arkane.provider.wallet.extraction;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletService;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletServiceFactory;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.domain.SecretKey;
import network.arkane.provider.wallet.extraction.request.AeternityKeystoreExtractionRequest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

@Component
public class AeternityKeystoreExtractor implements SecretExtractor<AeternityKeystoreExtractionRequest> {

    final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    final WalletService walletService = new WalletServiceFactory().getService();

    @Override
    public SecretKey extract(final AeternityKeystoreExtractionRequest importWalletRequest) {
        try {
            final byte[] privateKey = walletService.recoverPrivateKeyFromKeystore(importWalletRequest.getKeystore(), importWalletRequest.getPassword());
            final RawKeyPair recoveredRawKeypair = keyPairService.generateRawKeyPairFromSecret( Hex.toHexString( privateKey ) );
            return AeternitySecretKey.builder().keyPair(recoveredRawKeypair).build();
        } catch (AException e) {
            String msg = "Not a valid keystore file";
            if (e.getMessage() != null && e.getMessage().contains("wrong password")) {
                msg = "Wrong password provided for given keystore file";
            }
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public Class<AeternityKeystoreExtractionRequest> getImportRequestType() {
        return AeternityKeystoreExtractionRequest.class;
    }
}
