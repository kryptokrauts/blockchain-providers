package network.arkane.provider.wallet.generation;


import net.jodah.typetools.TypeResolver;
import network.arkane.provider.sign.Signer;
import network.arkane.provider.wallet.domain.SecretKey;

public interface WalletGenerator<T extends SecretKey> {

    /**
     * Generate a wallet, given a password and a secret
     *
     * @param password
     * @param secret
     * @return
     */
    GeneratedWallet generateWallet(final String password, final T secret);

    /**
     * The SecretKey this WalletGenerator supports
     *
     * @return
     */
    default Class<T> type() {
        return (Class<T>) TypeResolver.resolveRawArguments(WalletGenerator.class, getClass())[0];
    }
}
