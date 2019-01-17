package network.arkane.provider.sign;

import network.arkane.provider.BytesUtils;
import network.arkane.provider.JSONUtil;
import network.arkane.provider.Prefix;
import network.arkane.provider.clients.BlockchainClient;
import network.arkane.provider.core.model.clients.Address;
import network.arkane.provider.core.model.clients.Amount;
import network.arkane.provider.core.model.clients.RawTransaction;
import network.arkane.provider.core.model.clients.Revision;
import network.arkane.provider.core.model.clients.ToClause;
import network.arkane.provider.core.model.clients.ToData;
import network.arkane.provider.secret.generation.VechainSecretKey;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import network.arkane.provider.utils.CryptoUtils;
import network.arkane.provider.utils.RawTransactionFactory;
import network.arkane.provider.utils.crypto.ECDSASign;
import network.arkane.provider.wallet.decryption.VechainWalletDecryptor;
import network.arkane.provider.wallet.generation.GeneratedVechainWallet;
import org.springframework.stereotype.Component;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class VechainTransactionSigner implements Signer<VechainTransactionSignable, VechainSecretKey> {

    private VechainWalletDecryptor vechainWalletDecryptor;

    public VechainTransactionSigner(final VechainWalletDecryptor vechainWalletDecryptor) {
        this.vechainWalletDecryptor = vechainWalletDecryptor;
    }

    @Override
    public Signature createSignature(VechainTransactionSignable signable, VechainSecretKey key) {
        final RawTransaction rawTransaction = constructTransaction(signable);
        ECDSASign.SignatureData signature = ECDSASign.signMessage(rawTransaction.encode(),
                                                                  network.arkane.provider.utils.crypto.ECKeyPair.create(key.getKeyPair().getPrivateKey()),
                                                                  true);
        rawTransaction.setSignature(signature.toByteArray());
        final String fullSignBytes = BytesUtils.toHexString(rawTransaction.encode(), Prefix.ZeroLowerX);
        return TransactionSignature
                .signTransactionBuilder()
                .signedTransaction(fullSignBytes)
                .build();
    }

    @Override
    public VechainSecretKey reconstructKey(String secret, String password) {
        return vechainWalletDecryptor.generateKey(GeneratedVechainWallet.builder()
                                                                        .walletFile(JSONUtil.fromJson(secret, WalletFile.class))
                                                                        .build(), password);
    }

    private RawTransaction constructTransaction(final VechainTransactionSignable signable) {
        final Byte chainTag = !isEmpty(signable.getChainTag()) ? getChainTag() : Byte.valueOf(signable.getChainTag());
        byte[] blockRef = isEmpty(signable.getBlockRef()) ? getBlockRef() : BytesUtils.toByteArray(signable.getBlockRef());
        byte[] nonce = isEmpty(signable.getNonce()) ? CryptoUtils.generateTxNonce() : BytesUtils.toByteArray(signable.getNonce());
        return RawTransactionFactory.getInstance()
                                    .createRawTransaction(
                                            chainTag,
                                            blockRef,
                                            getExpiration(signable),
                                            signable.getGas(),
                                            (byte) signable.getGasPriceCoef().intValue(),
                                            nonce, convert(signable.getClauses()));
    }

    private int getExpiration(VechainTransactionSignable signable) {
        return signable.getExpiration() <= 0 ? 720 : signable.getExpiration();
    }

    private byte getChainTag() {
        try {
            return BlockchainClient.getChainTag();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch chainTag");
        }
    }


    private byte[] getBlockRef() {
        try {
            return BlockchainClient.getBlockRef(Revision.BEST).toByteArray();
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to fetch block ref");
        }
    }

    private ToClause[] convert(List<VechainTransactionSignableToClause> clauses) {
        return clauses
                .stream()
                .map(x -> {
                    Amount vet = Amount.VET();
                    if (x.getAmount() == null || x.getAmount().compareTo(BigInteger.ZERO) == 0) {
                        vet = Amount.ZERO;
                    } else {
                        vet.setBigIntegerAmount(x.getAmount());
                    }

                    ToData data;

                    if (x.getData() != null) {
                        data = new ToData();
                        data.setData(x.getData());
                    } else {
                        data = ToData.ZERO;
                    }
                    return new ToClause(Address.fromHexString(x.getTo()), vet, data);
                }).toArray(ToClause[]::new);
    }
}
