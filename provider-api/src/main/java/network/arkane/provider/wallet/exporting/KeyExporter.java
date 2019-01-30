package network.arkane.provider.wallet.exporting;

import net.jodah.typetools.TypeResolver;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

public interface KeyExporter<T extends SecretKey> {
    String export(T key, String password);

    /**
     * Reconstruct the key, based on the secret and the password
     *
     * @param secret
     * @param password
     * @return
     */
    T reconstructKey(final String secret, final String password);

    default Class<T> getType() {
        return (Class<T>) TypeResolver.resolveRawArguments(KeyExporter.class, getClass())[0];
    }

    /**
     * The type this secret exporter supports
     * @return
     */
    SecretType type();
}
