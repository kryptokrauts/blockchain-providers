package network.arkane.provider.neo.sign;

import io.neow3j.model.types.TransactionType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.AssetTransfer;
import lombok.SneakyThrows;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class NeoAssetTransferSigner implements Signer<NeoAssetTransferSignable, NeoSecretKey> {

    private Neow3j neow3j;
    private NeoRawSigner neoRawSigner;
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public NeoAssetTransferSigner(final Neow3j neow3j,
                                  final NeoRawSigner neoRawSigner) {
        this.neow3j = neow3j;
        this.neoRawSigner = neoRawSigner;
    }

    @Override
    @SneakyThrows
    public Signature createSignature(final NeoAssetTransferSignable signable,
                                     final NeoSecretKey key) {
        Account account = Account.fromECKeyPair(key.getKey()).build();
        executor.submit(() -> updateAccountAssetBalances(account));
        executor.submit(() -> updateAccountTokenBalances(account));
        executor.awaitTermination(10, TimeUnit.SECONDS);

        final ContractTransaction transaction = new AssetTransfer.Builder(neow3j)
                .toAddress(signable.getTo())
                .account(account)
                .amount(signable.getAmount())
                .asset(signable.getAssetId())
                .build().getTransaction();
        return neoRawSigner.createSignature(
                NeoRawSignable.builder()
                              .outputs(transaction.getOutputs())
                              .inputs(transaction.getInputs())
                              .attributes(transaction.getAttributes())
                              .transactionType(TransactionType.CONTRACT_TRANSACTION)
                              .build(),
                key);
    }

    @SneakyThrows
    private void updateAccountTokenBalances(Account account) {
        account.updateTokenBalances(neow3j);
    }

    @SneakyThrows
    private void updateAccountAssetBalances(Account account) {
        account.updateAssetBalances(neow3j);
    }
}
