package network.arkane.provider.nonfungable;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.NonFungibleGateway;
import network.arkane.provider.nonfungible.domain.NonFungible;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EthereumNonFungibleGateway implements NonFungibleGateway {

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }

    @Override
    public List<NonFungible> listNonFungibles(final String walletId, String[] contractAddresses) {
        return null;
    }
}
