package network.arkane.provider.sign;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.Prefix;
import network.arkane.provider.secret.generation.EthereumSecretKey;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.wallet.decryption.EthereumWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedEthereumWallet;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletFile;

@Component
public class EthereumTransactionSigner implements Signer<EthereumTransactionSignable, EthereumSecretKey> {
    private static final String DEFAULT_DATA = "0x";

    private EthereumWalletDecryptor ethereumWalletDecryptor;

    public EthereumTransactionSigner(final EthereumWalletDecryptor ethereumWalletDecryptor) {
        this.ethereumWalletDecryptor = ethereumWalletDecryptor;
    }

    @Override
    public Signature createSignature(EthereumTransactionSignable signable, EthereumSecretKey key) {
        final org.web3j.crypto.RawTransaction rawTransaction = constructTransaction(signable);
        byte[] encodedMessage = TransactionEncoder.signMessage(rawTransaction, Credentials.create(key.getKeyPair()));
        final String prettify = BytesUtils.withHexPrefix(Hex.toHexString(encodedMessage), Prefix.ZeroLowerX);
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(prettify)
                .build();
    }

    @Override
    public EthereumSecretKey reconstructKey(String secret, String password) {
        return ethereumWalletDecryptor.generateKey(GeneratedEthereumWallet.builder()
                                                                          .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                          .build(), password);
    }

    private org.web3j.crypto.RawTransaction constructTransaction(final EthereumTransactionSignable signTransactionRequest) {
        return org.web3j.crypto.RawTransaction.createTransaction(
                signTransactionRequest.getNonce(),
                signTransactionRequest.getGasPrice(),
                signTransactionRequest.getGasLimit(),
                signTransactionRequest.getTo(),
                signTransactionRequest.getValue(),
                StringUtils.isBlank(signTransactionRequest.getData()) ? DEFAULT_DATA : signTransactionRequest.getData());
    }
}
