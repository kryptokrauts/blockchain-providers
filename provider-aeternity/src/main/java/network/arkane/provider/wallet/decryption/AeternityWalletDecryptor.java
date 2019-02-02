package network.arkane.provider.wallet.decryption;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletService;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletServiceFactory;
import network.arkane.provider.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.generation.GeneratedAeternityWallet;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

@Component
public class AeternityWalletDecryptor implements WalletDecryptor<GeneratedAeternityWallet, AeternitySecretKey> {

    final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    final WalletService walletService = new WalletServiceFactory().getService();

    @Override
    public AeternitySecretKey generateKey(GeneratedAeternityWallet generatedWallet, String password) {
        try {
            final byte[] privateKey = walletService.recoverPrivateKeyFromKeystore(generatedWallet.getKeystoreJson(), password);
            final RawKeyPair recoveredRawKeypair = keyPairService.generateRawKeyPairFromSecret( Hex.toHexString( privateKey ) );
            return AeternitySecretKey.builder().keyPair(recoveredRawKeypair).build();
        } catch (AException e) {
            throw new IllegalArgumentException("Unable to fetch wallet");
        }
    }
}
