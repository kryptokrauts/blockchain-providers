package network.arkane.provider.tron.secret.generation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.tron.common.crypto.ECKey;
import org.web3j.crypto.ECKeyPair;

@Data
@NoArgsConstructor
public class TronSecretKey implements SecretKey {

    private ECKey keyPair;

    @Override
    public SecretType type() {
        return SecretType.TRON;
    }

    @Builder
    public TronSecretKey(ECKey keyPair) {
        this.keyPair = keyPair;
    }
}
