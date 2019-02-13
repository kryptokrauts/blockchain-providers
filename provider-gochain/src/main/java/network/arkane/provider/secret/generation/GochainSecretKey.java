package network.arkane.provider.secret.generation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;
import org.web3j.crypto.ECKeyPair;

@Data
@NoArgsConstructor
public class GochainSecretKey implements SecretKey {

    private ECKeyPair keyPair;

    @Override
    public SecretType type() {
        return SecretType.GOCHAIN;
    }

    @Builder
    public GochainSecretKey(ECKeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
