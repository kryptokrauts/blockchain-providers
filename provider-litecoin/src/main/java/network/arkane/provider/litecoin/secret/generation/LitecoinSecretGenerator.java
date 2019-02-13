package network.arkane.provider.litecoin.secret.generation;

import lombok.SneakyThrows;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.SecretGenerator;
import org.bitcoinj.core.ECKey;
import org.springframework.stereotype.Component;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

@Component
public class LitecoinSecretGenerator implements SecretGenerator<LitecoinSecretKey> {

    @Override
    @SneakyThrows
    public LitecoinSecretKey generate() {
        final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        ECKey keyPair = ECKey.fromPrivate(ecKeyPair.getPrivateKey());
        return new LitecoinSecretKey(keyPair);
    }

    @Override
    public SecretType type() {
        return SecretType.LITECOIN;
    }
}
