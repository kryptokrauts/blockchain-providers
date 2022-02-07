package network.arkane.provider.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;

import java.util.List;

@Slf4j
public abstract class EvmNonFungibleGateway implements NonFungibleGateway {

    private final EvmNonFungibleStrategy strategy;

    public EvmNonFungibleGateway(final List<EvmNonFungibleStrategy> strategies) {
        this.strategy = strategies.stream().filter(b -> b.getSecretType() == getSecretType()).findFirst().orElseThrow();
    }

    @Override
    public List<NonFungibleAssetBalance> listNonFungibles(String walletAddress,
                                                          String... contractAddresses) {
        return strategy.listNonFungibles(walletAddress, contractAddresses);
    }

    @Override
    public NonFungibleAsset getNonFungible(String contractAddress,
                                           String tokenId) {
        return strategy.getNonFungible(contractAddress, tokenId);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(String contractAddress) {
        return strategy.getNonFungibleContract(contractAddress);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(String contractAddress, boolean forceUpdate) {
        return strategy.getNonFungibleContract(contractAddress, forceUpdate);
    }
}
