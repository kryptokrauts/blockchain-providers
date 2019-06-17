package network.arkane.provider.nonfungible;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungible;

import java.util.List;

public interface NonFungibleGateway {

    SecretType getSecretType();

    List<NonFungible> listNonFungibles(String walletId, String... contractAddresses);
}
