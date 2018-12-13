package network.arkane.provider.wallet.generation;


import network.arkane.provider.chain.SecretType;
import network.arkane.provider.wallet.domain.SecretKey;

public interface AbstractWalletGenerator {

    GeneratedWallet generateWallet(final String password, final SecretKey secret);

    SecretType type();
}
