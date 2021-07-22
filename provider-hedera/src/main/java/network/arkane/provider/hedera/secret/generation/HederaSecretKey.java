package network.arkane.provider.hedera.secret.generation;

import com.hedera.hashgraph.sdk.PrivateKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class HederaSecretKey implements SecretKey {

    private PrivateKey key;

    @Override
    public SecretType type() {
        return SecretType.HEDERA;
    }


}
