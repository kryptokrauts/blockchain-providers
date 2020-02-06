package network.arkane.provider.nonfungible;

import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VechainNonFungibleGateway implements NonFungibleGateway {

    final BusinessNonFungibleGateway businessNonFungibleGateway;

    public VechainNonFungibleGateway(final BusinessNonFungibleGateway businessNonFungibleGateway) {
        this.businessNonFungibleGateway = businessNonFungibleGateway;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.VECHAIN;
    }

    @Override
    public List<NonFungibleAsset> listNonFungibles(final String walletId,
                                                   final String... contractAddresses) {
        return businessNonFungibleGateway.listNonFungibles(getSecretType(), walletId, contractAddresses);
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return businessNonFungibleGateway.getNonFungible(getSecretType(), contractAddress, tokenId);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return businessNonFungibleGateway.getNonFungibleContract(getSecretType(), contractAddress);
    }
}
