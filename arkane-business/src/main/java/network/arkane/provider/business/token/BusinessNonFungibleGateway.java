package network.arkane.provider.business.token;

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
public class BusinessNonFungibleGateway {

    public static final String CONTRACT_TYPE = "ERC_1155";
    final BusinessTokenGateway businessTokenGateway;

    public BusinessNonFungibleGateway(final BusinessTokenGateway businessTokenGateway) {
        this.businessTokenGateway = businessTokenGateway;
    }

    public List<NonFungibleAsset> listNonFungibles(final SecretType secretType,
                                                   final String walletId,
                                                   final String... contractAddresses) {
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
                                                                                        .url(x.getUrl())
                                                                                        .imageUrl(x.getImageUrl())
                                                                                        .type(CONTRACT_TYPE)
                                                                                        .build())
                                                             .description(x.getTokenType().getDescription())
                                                             .name(x.getTokenType().getName())
                                                             .imageUrl(x.getImageUrl())
                                                             .imagePreviewUrl(x.getImagePreviewUrl())
                                                             .imageThumbnailUrl(x.getImageThumbnailUrl())
                                                             .build()).collect(Collectors.toList());
    }

    public NonFungibleAsset getNonFungible(final SecretType secretType,
                                           final String contractAddress,
                                           final String tokenId) {
        final TokenDto token = businessTokenGateway.getToken(contractAddress, new BigInteger(tokenId));
        if (token == null || token.getSecretType() != secretType) {
            return null;
        }
        return NonFungibleAsset.builder()
                               .tokenId(tokenId)
                               .imagePreviewUrl(token.getImagePreviewUrl())
                               .contract(getNonFungibleContract(secretType, contractAddress))
                               .backgroundColor(token.getBackgroundColor())
                               .imageThumbnailUrl(token.getImageThumbnailUrl())
                               .imageUrl(token.getImageUrl())
                               .url(token.getUrl())
                               .name(token.getTokenType().getName())
                               .description(token.getTokenType().getDescription())
                               .build();
    }

    public NonFungibleContract getNonFungibleContract(final SecretType secretType,
                                                      final String contractAddress) {
        final TokenContract contract = businessTokenGateway.getContract(contractAddress);
        if (contract == null || contract.getSecretType() != secretType) {
            return null;
        }
        return NonFungibleContract.builder()
                                  .address(contract.getAddress())
                                  .description(contract.getDescription())
                                  .name(contract.getName())
                                  .type(CONTRACT_TYPE)
                                  .build();
    }
}
