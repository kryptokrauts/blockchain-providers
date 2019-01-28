package network.arkane.provider.wallet.exporting;

import net.jodah.typetools.TypeResolver;
import network.arkane.provider.wallet.domain.SecretKey;

public interface KeyExporter<T extends SecretKey> {
    String export(T key, String password);

    default Class<T> getType() {
        return (Class<T>) TypeResolver.resolveRawArguments(KeyExporter.class, getClass())[0];
    }
}
