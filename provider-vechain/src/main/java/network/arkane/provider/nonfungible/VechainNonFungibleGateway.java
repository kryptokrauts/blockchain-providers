package network.arkane.provider.nonfungible;

import network.arkane.business.token.BusinessTokenGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.core.model.clients.ERC1155ContractClient;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VechainNonFungibleGateway implements NonFungibleGateway {

    final BusinessTokenGateway businessTokenGateway;

    public VechainNonFungibleGateway(final BusinessTokenGateway businessTokenGateway) {
        this.businessTokenGateway = businessTokenGateway;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.VECHAIN;
    }

    @Override
    public List<NonFungibleAsset> listNonFungibles(final String walletId, final String... contractAddresses) {
        return businessTokenGateway.getTokensForAddress(walletId)
                                   .stream()
                                   .filter(x -> x.getTokenType().isNf())
                                   .map(x -> {
                                       return NonFungibleAsset.builder()
                                                              .tokenId(x.getTokenId().toString())
                                                              .contract(NonFungibleContract.builder().build())
                                                              .description(x.getTokenType().getDescription())
                                                              .name(x.getTokenType().getName())
                                                              .build();
                                   }).collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress, final String tokenId) {

    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        throw new IllegalArgumentException("Not Implemented");
    }
}