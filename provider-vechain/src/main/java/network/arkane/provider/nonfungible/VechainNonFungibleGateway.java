package network.arkane.provider.nonfungible;

import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VechainNonFungibleGateway implements NonFungibleGateway {

    @Override
    public SecretType getSecretType() {
        return SecretType.VECHAIN;
    }

    @Override
    public List<NonFungibleAssetBalance> listNonFungibles(final String walletId,
                                                          final String... contractAddresses) {
        return null;
        //        return businessNonFungibleGateway.listNonFungibles(getSecretType(), walletId, contractAddresses)
        //                                         .stream()
        //                                         .map(asset -> NonFungibleAssetBalance.from(asset, BigInteger.ONE, null))
        //                                         .collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return null;
        //        return businessNonFungibleGateway.getNonFungible(getSecretType(), contractAddress, tokenId);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return null;
        //return businessNonFungibleGateway.getNonFungibleContract(getSecretType(), contractAddress);
    }
}
