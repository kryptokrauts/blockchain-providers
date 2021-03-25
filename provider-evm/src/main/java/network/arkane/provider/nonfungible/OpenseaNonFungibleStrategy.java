package network.arkane.provider.nonfungible;

import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.opensea.NonFungibleContractTypeMapper;
import network.arkane.provider.opensea.OpenSeaAssetToNonFungibleAssetMapper;
import network.arkane.provider.opensea.OpenSeaContractToNonFungibleContractMapper;
import network.arkane.provider.opensea.OpenSeaGateway;
import network.arkane.provider.web3j.EvmWeb3jGateway;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public abstract class OpenseaNonFungibleStrategy implements EvmNonFungibleStrategy {

    private final OpenSeaGateway openSeaGateway;
    private final OpenSeaAssetToNonFungibleAssetMapper mapper;
    private final OpenSeaContractToNonFungibleContractMapper contractMapper;

    public OpenseaNonFungibleStrategy(final EvmWeb3jGateway evmWeb3jGateway,
                                      final OpenSeaGateway openSeaGateway) {
        this.openSeaGateway = openSeaGateway;
        this.contractMapper = new OpenSeaContractToNonFungibleContractMapper(new NonFungibleContractTypeMapper(evmWeb3jGateway));
        this.mapper = new OpenSeaAssetToNonFungibleAssetMapper(this.contractMapper);
    }

    @Override
    public List<NonFungibleAssetBalance> listNonFungibles(final String walletAddress,
                                                          final String... contractAddresses) {
        return mapper.mapToList(openSeaGateway.listAssets(walletAddress, contractAddresses))
                     .stream()
                     .map(asset -> NonFungibleAssetBalance.from(asset, BigInteger.ONE))
                     .collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return mapper.map(openSeaGateway.getAsset(contractAddress, tokenId));
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return contractMapper.map(openSeaGateway.getContract(contractAddress));
    }
}
