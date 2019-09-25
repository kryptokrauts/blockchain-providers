package network.arkane.provider.neo.sign;

import io.neow3j.protocol.Neow3j;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;
import lombok.SneakyThrows;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

@Component
public class NeoAssetTransferSigner implements Signer<NeoAssetTransferSignable, NeoSecretKey> {

    private Neow3j neow3j;

    public NeoAssetTransferSigner(final Neow3j neow3j) {
        this.neow3j = neow3j;
    }

    @Override
    @SneakyThrows
    public Signature createSignature(final NeoAssetTransferSignable signable,
                                     final NeoSecretKey key) {
        Account account = Account.fromECKeyPair(key.getKey()).build();
        updateAccountAssetBalances(account);

        final ContractTransaction transaction = new AssetTransfer.Builder(neow3j)
                .toAddress(signable.getTo())
                .account(account)
                .amount(signable.getAmount().toString())
                .asset(signable.getAssetId())
                .build().sign().getTransaction();

        String rawTx = Numeric.toHexStringNoPrefix(transaction.toArray());
        return TransactionSignature.signTransactionBuilder().signedTransaction(rawTx).build();
    }

    @SneakyThrows
    private void updateAccountAssetBalances(Account account) {
        account.updateAssetBalances(neow3j);
    }
}
