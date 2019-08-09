package network.arkane.provider.neo.sign;

import io.neow3j.model.types.NEOAsset;
import io.neow3j.protocol.Neow3j;
import io.neow3j.transaction.ContractTransaction;
import io.neow3j.wallet.AssetTransfer;
import network.arkane.provider.neo.secret.generation.NeoSecretKey;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.sign.domain.Signature;
import org.springframework.stereotype.Component;

@Component
public class NeoAssetTransferSigner implements Signer<NeoAssetTransferSignable, NeoSecretKey> {

    private Neow3j neow3j;
    private NeoTransactionSigner neoTransactionSigner;

    public NeoAssetTransferSigner(final Neow3j neow3j,
                                  final NeoTransactionSigner neoTransactionSigner) {
        this.neow3j = neow3j;
        this.neoTransactionSigner = neoTransactionSigner;
    }

    @Override
    public Signature createSignature(NeoAssetTransferSignable signable, NeoSecretKey key) {
        ContractTransaction transaction = new AssetTransfer.Builder(neow3j)
                .toAddress(signable.getTo())
                .amount(signable.getAmount())
                .asset(NEOAsset.HASH_ID)
                .build().getTransaction();
        return neoTransactionSigner.createSignature(
                NeoTransactionSignable.builder()
                                      .outputs(transaction.getOutputs())
                                      .inputs(transaction.getInputs())
                                      .attributes(transaction.getAttributes())
                                      .build(),
                key);
    }
}
