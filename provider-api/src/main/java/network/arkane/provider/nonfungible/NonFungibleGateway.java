package network.arkane.provider.nonfungible;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;

import java.util.List;

public interface NonFungibleGateway {

    SecretType getSecretType();

    List<NonFungibleAssetBalance> listNonFungibles(String walletAddress,
                                                   String... contractAddresses);

    NonFungibleAsset getNonFungible(String contractAddress, String tokenId);

    NonFungibleContract getNonFungibleContract(String contractAddress);

    default NonFungibleContract getNonFungibleContract(String contractAddress, boolean forceUpdate) {
        return getNonFungibleContract(contractAddress);
    }
}
