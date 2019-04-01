package network.arkane.provider.tron.secret.generation;

import org.tron.common.crypto.ECKey;

import java.security.SecureRandom;

public class TronSecretKeyMother {

    public static TronSecretKey aTronSecretKey() {
        return TronSecretKey.builder()
                            .keyPair(new ECKey(new SecureRandom("test".getBytes())))
                            .build();
    }
}