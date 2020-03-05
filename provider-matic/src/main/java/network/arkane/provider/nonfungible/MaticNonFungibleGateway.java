package network.arkane.provider.nonfungible;

import lombok.SneakyThrows;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.ERC1155BlockscoutToken;
import network.arkane.blockchainproviders.blockscout.dto.ERC721BlockscoutToken;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaticNonFungibleGateway implements NonFungibleGateway {

    private BlockscoutClient maticBlockscoutClient;
    private MetaDataParser metadataParser;

    public MaticNonFungibleGateway(BlockscoutClient maticBlockscoutClient,
                                   MetaDataParser metadataParser) {
        this.maticBlockscoutClient = maticBlockscoutClient;
        this.metadataParser = metadataParser;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }

    @Override
    @SneakyThrows
    public List<NonFungibleAsset> listNonFungibles(final String walletAddress,
                                                   final String... contractAddresses) {
        Set<String> contracts = contractAddresses == null ? new HashSet<>() : Arrays.stream(contractAddresses).map(String::toLowerCase).collect(Collectors.toSet());

        return maticBlockscoutClient.getTokenBalances(walletAddress)
                                    .stream()
                                    .filter(t -> t.getType().equalsIgnoreCase("ERC-721") || t.getType().equalsIgnoreCase("ERC-1155"))
                                    .filter(x -> contractAddresses == null || contractAddresses.length == 0 || contracts.contains(x.getContractAddress().toLowerCase()))
                                    .map(t -> t.getType().equalsIgnoreCase("ERC-721")
                                              ? mapERC721(walletAddress, (ERC721BlockscoutToken) t)
                                              : mapERC1155(walletAddress, (ERC1155BlockscoutToken) t))
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toList());

    }

    private List<NonFungibleAsset> mapERC721(final String walletAddress,
                                             ERC721BlockscoutToken token) {
        NonFungibleContract contract = createContract(token);
        return token.getTokens()
                    .stream()
                    .map(x -> getNonFungibleAsset(x.getTokenId().toString(), contract))
                    .collect(Collectors.toList());

    }

    private List<NonFungibleAsset> mapERC1155(final String walletAddress,
                                              ERC1155BlockscoutToken token) {
        NonFungibleContract contract = createContract(token);
        return token.getTokens()
                    .stream()
                    .map(x -> getNonFungibleAsset(x.getTokenId().toString(), contract)
                        )
                    .collect(Collectors.toList());

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
                                  .type(token.getType())
                                  .build();
    }

    @Override
    @SneakyThrows
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {

        NonFungibleContract contract = getNonFungibleContract(contractAddress);
        if (contract != null) {
            return getNonFungibleAsset(tokenId, contract);
        }
        return null;

    }

    private NonFungibleAsset getNonFungibleAsset(String tokenId,
                                                 NonFungibleContract contract) {
        NonFungibleMetaData metaData = metadataParser.parseMetaData(tokenId, contract);
        if (metaData != null) {
            return NonFungibleAsset.builder()
                                   .name(metaData.getName())
                                   .imageUrl(metaData.getImage())
                                   .imagePreviewUrl(metaData.getImage())
                                   .imageThumbnailUrl(metaData.getImage())
                                   .tokenId(tokenId)
                                   .contract(contract)
                                   .description(metaData.getDescription())
                                   .url(metaData.getProperty("url"))
                                   .build();
        }
        return null;
    }


    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return maticBlockscoutClient.getTokenInfo(contractAddress)
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
