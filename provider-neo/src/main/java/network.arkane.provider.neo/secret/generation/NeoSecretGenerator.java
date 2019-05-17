package network.arkane.provider.neo.secret.generation;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.secret.generation.SecretGenerator;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import io.neow3j.crypto.ECKeyPair;
import io.neow3j.crypto.Keys;

public class NeoSecretGenerator implements SecretGenerator<NeoSecretKey> {

    @Override
    public NeoSecretKey generate() {
        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            return new NeoSecretKey(ecKeyPair);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SecretType type() {
        return SecretType.NEO;
    }


}
