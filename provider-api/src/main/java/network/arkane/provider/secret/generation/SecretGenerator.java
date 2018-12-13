package network.arkane.provider.secret.generation;


import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

public interface SecretGenerator<T extends SecretKey> {

    T generate();

    SecretType type();
}
