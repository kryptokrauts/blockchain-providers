package network.arkane.provider.nonfungible;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.ERC1155BlockscoutToken;
import network.arkane.blockchainproviders.blockscout.dto.ERC721BlockscoutToken;
import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.contract.EvmContractService;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.cache.CacheManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class BlockscoutNonFungibleStrategy implements NonFungibleGateway {

    private BlockscoutClient blockscoutClient;
    private MetaDataParser metadataParser;
    private BusinessNonFungibleGateway businessNonFungibleGateway;


    public BlockscoutNonFungibleStrategy(BlockscoutClient blockscoutClient,
                                         EvmContractService contractService,
                                         BusinessNonFungibleGateway businessNonFungibleGateway,
                                         Optional<CacheManager> cacheManager) {
        this.blockscoutClient = blockscoutClient;
        this.metadataParser = new MetaDataParser(contractService, cacheManager);
        this.businessNonFungibleGateway = businessNonFungibleGateway;
    }

    @Override
    @SneakyThrows
    public List<NonFungibleAsset> listNonFungibles(final String walletAddress,
                                                   final String... contractAddresses) {
        Set<String> contracts = contractAddresses == null ? new HashSet<>() : Arrays.stream(contractAddresses).map(String::toLowerCase).collect(Collectors.toSet());

        return blockscoutClient.getTokenBalances(walletAddress)
                               .stream()
                               .filter(t -> t.getType().endsWith("721") || t.getType().endsWith("1155"))
                               .filter(x -> contractAddresses == null || contractAddresses.length == 0 || contracts.contains(x.getContractAddress().toLowerCase()))
                               .map(t -> t.getType().endsWith("721")
                                         ? mapERC721((ERC721BlockscoutToken) t)
                                         : mapERC1155((ERC1155BlockscoutToken) t))
                               .flatMap(Collection::stream)
                               .collect(Collectors.toList());

    }

    private List<NonFungibleAsset> mapERC721(ERC721BlockscoutToken token) {
        NonFungibleContract contract = createContract(token);
        return token.getTokens() == null
               ? Collections.emptyList()
               : token.getTokens()
                      .stream()
                      .filter(x -> x.getBalance() != null && x.getBalance().compareTo(BigInteger.ZERO) > 0)
                      .map(x -> getNonFungibleAsset(x.getTokenId().toString(), contract, token))
                      .collect(Collectors.toList());

    }

    private List<NonFungibleAsset> mapERC1155(ERC1155BlockscoutToken token) {

        NonFungibleContract contract = createContract(token);
        return token.getTokens() == null
               ? Collections.emptyList()
               : token.getTokens()
                      .stream()
                      .filter(x -> x.getBalance() != null && x.getBalance().compareTo(BigInteger.ZERO) > 0)
                      .map(x -> {
                               if (isBusinessToken(token.getContractAddress())) {
                                   return businessNonFungibleGateway.getNonFungible(getSecretType(),
                                                                                    token.getContractAddress(),
                                                                                    x.getTokenId().toString());
                               } else {
                                   return getNonFungibleAsset(x.getTokenId().toString(), contract);
                               }
                           }
                          )
                      .collect(Collectors.toList());

    }

    private boolean isBusinessToken(String contractAddress) {
        try {
            return businessNonFungibleGateway.getNonFungibleContract(getSecretType(), contractAddress) != null;
        } catch (Exception e) {
            log.error("Error getting business contract", e);
            return false;
        }
    }

    private NonFungibleContract createContract(ERC721BlockscoutToken token) {
        return NonFungibleContract.builder()
                                  .type("ERC-721")
                                  .address(token.getContractAddress())
                                  .type(token.getType())
                                  .name(token.getName())
                                  .symbol(token.getSymbol())
                                  .build();
    }

    private NonFungibleContract createContract(ERC1155BlockscoutToken token) {
        return NonFungibleContract.builder()
                                  .type("ERC-1155")
                                  .address(token.getContractAddress())
                                  .name(token.getContractAddress())
                                  .type(token.getType())
                                  .build();
    }

    @Override
    @SneakyThrows
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {

        NonFungibleContract contract = getNonFungibleContract(contractAddress);
        if (contract != null) {
            if (isBusinessToken(contract.getAddress())) {
                return businessNonFungibleGateway.getNonFungible(getSecretType(),
                                                                 contract.getAddress(),
                                                                 tokenId);
            }
            return getNonFungibleAsset(tokenId, contract);
        }
        return null;

    }

    private NonFungibleAsset getNonFungibleAsset(String tokenId,
                                                 NonFungibleContract contract,
                                                 ERC721BlockscoutToken token) {
        NonFungibleMetaData metaData = metadataParser.parseMetaData(getSecretType(), tokenId, contract.getType(), contract.getAddress());
        if (metaData != null) {
            return NonFungibleAsset.builder()
                                   .name(metaData.getName())
                                   .imageUrl(metaData.getImage().orElse(null))
                                   .imagePreviewUrl(metaData.getImage().orElse(null))
                                   .imageThumbnailUrl(metaData.getImage().orElse(null))
                                   .tokenId(tokenId)
                                   .contract(contract)
                                   .description(metaData.getDescription())
                                   .url(metaData.getExternalUrl().orElse(null))
                                   .animationUrl(metaData.getAnimationUrl().orElse(null))
                                   .build();
        }
        return NonFungibleAsset.builder()
                               .name(token.getName())
                               .tokenId(tokenId)
                               .contract(contract)
                               .build();
    }

    private NonFungibleAsset getNonFungibleAsset(String tokenId,
                                                 NonFungibleContract contract) {
        NonFungibleMetaData metaData = metadataParser.parseMetaData(getSecretType(), tokenId, contract.getType(), contract.getAddress());
        if (metaData != null) {
            return NonFungibleAsset.builder()
                                   .name(metaData.getName())
                                   .imageUrl(metaData.getImage().orElse(null))
                                   .imagePreviewUrl(metaData.getImage().orElse(null))
                                   .imageThumbnailUrl(metaData.getImage().orElse(null))
                                   .tokenId(tokenId)
                                   .contract(contract)
                                   .description(metaData.getDescription())
                                   .url(metaData.getExternalUrl().orElse(null))
                                   .animationUrl(metaData.getAnimationUrl().orElse(null))
                                   .build();
        }
        return NonFungibleAsset.builder()
                               .tokenId(tokenId)
                               .contract(contract)
                               .name(tokenId)
                               .description(contract.getAddress())
                               .build();
    }


    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return blockscoutClient.getTokenInfo(contractAddress)
                               .map(token -> NonFungibleContract.builder()
                                                                .type(token.getType())
                                                                .address(contractAddress)
                                                                .type(token.getType())
                                                                .name(token.getName())
                                                                .symbol(token.getSymbol())
                                                                .build())
                               .orElse(null);
    }

}
