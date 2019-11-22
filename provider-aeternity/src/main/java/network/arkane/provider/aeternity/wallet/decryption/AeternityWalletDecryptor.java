package network.arkane.provider.aeternity.wallet.decryption;

import com.kryptokrauts.aeternity.sdk.domain.secret.impl.RawKeyPair;
import com.kryptokrauts.aeternity.sdk.exception.AException;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairService;
import com.kryptokrauts.aeternity.sdk.service.keypair.KeyPairServiceFactory;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletService;
import com.kryptokrauts.aeternity.sdk.service.wallet.WalletServiceFactory;
import network.arkane.provider.aeternity.wallet.generation.GeneratedAeternityWallet;
import network.arkane.provider.aeternity.secret.generation.AeternitySecretKey;
import network.arkane.provider.wallet.decryption.WalletDecryptor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

@Component
public class AeternityWalletDecryptor implements WalletDecryptor<GeneratedAeternityWallet, AeternitySecretKey> {

    private final KeyPairService keyPairService = new KeyPairServiceFactory().getService();
    private final WalletService walletService = new WalletServiceFactory().getService();

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
