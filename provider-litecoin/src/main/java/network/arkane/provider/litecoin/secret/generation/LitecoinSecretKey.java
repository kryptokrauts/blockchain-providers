package network.arkane.provider.litecoin.secret.generation;

import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.bitcoinj.core.ECKey;

@Data
@NoArgsConstructor
public class LitecoinSecretKey implements SecretKey {

    private ECKey key;

    public LitecoinSecretKey(ECKey key) {
        this.key = key;
    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }
}
