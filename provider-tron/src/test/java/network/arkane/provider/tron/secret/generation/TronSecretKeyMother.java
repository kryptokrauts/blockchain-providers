package network.arkane.provider.tron.secret.generation;

import org.tron.common.crypto.ECKey;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TronSecretKeyMother {

    public static TronSecretKey aRandomSecretKey() {
        return TronSecretKey.builder()
                            .keyPair(new ECKey(new SecureRandom("test".getBytes())))
                            .build();
    }

    public static TronSecretKey aDeterministicSecretKey() {
        return TronSecretKey.builder()
                            .keyPair(ECKey.fromPrivate(BigInteger.ONE))
                            .build();
    }
}