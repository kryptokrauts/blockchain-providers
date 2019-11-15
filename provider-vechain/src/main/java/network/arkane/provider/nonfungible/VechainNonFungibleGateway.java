package network.arkane.provider.nonfungible;

import network.arkane.provider.business.token.BusinessTokenGateway;
import network.arkane.provider.business.token.model.TokenContract;
import network.arkane.provider.business.token.model.TokenDto;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
                                   .map(x -> NonFungibleAsset.builder()
                                                          .tokenId(x.getContractTokenId().toString())
                                                          .contract(
                                                                  NonFungibleContract.builder()
                                                                                     .name(x.getTokenType().getTokenContract().getName())
                                                                                     .description(x.getTokenType().getTokenContract().getDescription())
                                                                                     .address(x.getTokenType().getTokenContract().getAddress())
                                                                                     .symbol(null)
                                                                                     .url(null)
                                                                                     .imageUrl(null)
                                                                                     .type("ERC_1155")
                                                                                     .build())
                                                          .description(x.getTokenType().getDescription())
                                                          .name(x.getTokenType().getName())
                                                          .imageUrl(x.getImageUrl())
                                                          .imagePreviewUrl(x.getImagePreviewUrl())
                                                          .imageThumbnailUrl(x.getImageThumbnailUrl())
                                                          .build()).collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress, final String tokenId) {
        final TokenDto token = businessTokenGateway.getToken(contractAddress, new BigInteger(tokenId));
        return NonFungibleAsset.builder()
                               .tokenId(tokenId)
                               .imagePreviewUrl(token.getImagePreviewUrl())
                               .contract(getNonFungibleContract(contractAddress))
                               .backgroundColor(token.getBackgroundColor())
                               .imageThumbnailUrl(token.getImageThumbnailUrl())
                               .imageUrl(token.getImageUrl())
                               .url(token.getUrl())
                               .name(token.getTokenType().getName())
                               .description(token.getTokenType().getDescription())
                               .build();
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        final TokenContract contract = businessTokenGateway.getContract(contractAddress);
        return NonFungibleContract.builder()
                                  .address(contract.getAddress())
                                  .description(contract.getDescription())
                                  .name(contract.getName())
                                  .build();
    }
}
