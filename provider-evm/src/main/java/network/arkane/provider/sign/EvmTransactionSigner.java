package network.arkane.provider.sign;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.Prefix;
import network.arkane.provider.config.EvmProperties;
import network.arkane.provider.secret.generation.EvmSecretKey;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;

@Component
public class EvmTransactionSigner implements Signer<EvmTransactionSignable, EvmSecretKey> {
    private static final String DEFAULT_DATA = "0x";

    private final EvmProperties properties;

    public EvmTransactionSigner(EvmProperties properties) {
        this.properties = properties;
    }

    @Override
    public Signature createSignature(final EvmTransactionSignable signable,
                                     final EvmSecretKey key) {
        final org.web3j.crypto.RawTransaction rawTransaction = constructTransaction(signable);
        Long chainId = properties.getChainIds().get(key.getType());
        byte[] encodedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, Credentials.create(key.getKeyPair()));
        final String prettify = BytesUtils.withHexPrefix(Hex.toHexString(encodedMessage), Prefix.ZeroLowerX);
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(prettify)
                .build();
    }

    private org.web3j.crypto.RawTransaction constructTransaction(final EvmTransactionSignable signTransactionRequest) {
        return org.web3j.crypto.RawTransaction.createTransaction(
                signTransactionRequest.getNonce(),
                signTransactionRequest.getGasPrice(),
                signTransactionRequest.getGasLimit(),
                signTransactionRequest.getTo(),
                signTransactionRequest.getValue(),
                StringUtils.isBlank(signTransactionRequest.getData()) ? DEFAULT_DATA : signTransactionRequest.getData());
    }
}
