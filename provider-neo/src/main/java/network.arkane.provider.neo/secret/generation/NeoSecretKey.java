package network.arkane.provider.neo.secret.generation;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

import io.neow3j.crypto.ECKeyPair;

@Data
@NoArgsConstructor
public class NeoSecretKey implements SecretKey {

    private ECKeyPair key;

    @Override
    public SecretType type() {
        return SecretType.NEO;
    }

    @Builder
    public NeoSecretKey(final ECKeyPair key) {
        this.key = key;
    }
}
