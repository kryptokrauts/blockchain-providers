package network.arkane.provider.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.sign.domain.HexSignature;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.wallet.decryption.EthereumWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedEthereumWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Sign;
import org.web3j.crypto.WalletFile;

import static network.arkane.provider.exceptions.ArkaneException.arkaneException;

@Slf4j
@Component
public class EthereumRawSigner extends Signer<EthereumRawSignable, EthereumSecretKey> {

    private EthereumWalletDecryptor ethereumWalletDecryptor;

    public EthereumRawSigner(EthereumWalletDecryptor ethereumWalletDecryptor) {
        super(EthereumRawSignable.class);
        this.ethereumWalletDecryptor = ethereumWalletDecryptor;
    }

    @Override
    public Signature createSignature(EthereumRawSignable signable, EthereumSecretKey key) {
        try {
            final Sign.SignatureData signatureData = Sign.signMessage(signable.getData().getBytes("UTF-8"), key.getKeyPair());
            return HexSignature
                    .builder()
                    .r(signatureData.getR())
                    .s(signatureData.getS())
                    .v(signatureData.getV())
                    .build();
        } catch (final Exception ex) {
            log.error("Unable to sign transaction: {}", ex.getMessage());
            throw arkaneException()
                    .errorCode("transaction.sign.internal-error")
                    .errorCode("A problem occurred trying to sign the raw Ethereum object")
                    .cause(ex)
                    .build();
        }
    }

    @Override
    public EthereumSecretKey reconstructKey(String secret, String password) {
        return ethereumWalletDecryptor.generateKey(GeneratedEthereumWallet.builder()
                                                                          .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                          .build(), password);
    }
}
