package network.arkane.provider.nonfungible;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.blockchainproviders.azrael.AzraelClient;
import network.arkane.blockchainproviders.azrael.dto.ContractType;
import network.arkane.blockchainproviders.azrael.dto.TokenBalance;
import network.arkane.blockchainproviders.azrael.dto.token.erc1155.Erc1155TokenBalances;
import network.arkane.blockchainproviders.azrael.dto.token.erc721.Erc721TokenBalances;
import network.arkane.provider.contract.EvmContractService;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public abstract class AzraelNonFungibleStrategy implements EvmNonFungibleStrategy {

    private final AzraelClient azraelClient;
    private final MetaDataParser metadataParser;


    public AzraelNonFungibleStrategy(AzraelClient azraelClient,
                                     EvmContractService contractService,
                                     Optional<CacheManager> cacheManager) {
        this.azraelClient = azraelClient;
        this.metadataParser = new MetaDataParser(contractService, cacheManager);
    }

    @Override
    @SneakyThrows
    public List<NonFungibleAsset> listNonFungibles(final String walletAddress,
                                                   final String... contractAddresses) {
        Set<String> contracts = contractAddresses == null ? new HashSet<>() : Arrays.stream(contractAddresses).map(String::toLowerCase).collect(Collectors.toSet());
        List<TokenBalance> tokens = azraelClient.getTokens(walletAddress, Arrays.asList(ContractType.ERC_721, ContractType.ERC_1155));
        if (CollectionUtils.isEmpty(tokens)) return Collections.emptyList();

        List<Callable<NonFungibleAsset>> calls = tokens
                .stream()
                .filter(x -> contractAddresses == null || contractAddresses.length == 0 || contracts.contains(x.getAddress().toLowerCase()))
                .map(t -> t.getType() == ContractType.ERC_721
                          ? mapERC721((Erc721TokenBalances) t)
                          : mapERC1155((Erc1155TokenBalances) t))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(Integer.min(calls.size(), 25));
        List<NonFungibleAsset> result = executorService.invokeAll(calls).stream().map(x -> {
            try {
                return x.get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }).collect(Collectors.toList());
        executorService.shutdownNow();
        return result;
    }

    protected List<Callable<NonFungibleAsset>> mapERC721(Erc721TokenBalances token) {
        NonFungibleContract contract = createContract(token);
        return token.getTokens() == null
               ? Collections.emptyList()
               : token.getTokens()
                      .stream()
                      .filter(x -> x.getBalance() != null && x.getBalance().compareTo(BigInteger.ZERO) > 0)
                      .map(tb -> (Callable<NonFungibleAsset>) () -> getNonFungibleAsset(tb.getTokenId().toString(), contract, tb.getMetadata()))
                      .collect(Collectors.toList());

    }

    protected List<Callable<NonFungibleAsset>> mapERC1155(Erc1155TokenBalances token) {
        NonFungibleContract contract = createContract(token);
        return token.getTokens() == null
               ? Collections.emptyList()
               : token.getTokens()
                      .stream()
                      .filter(x -> x.getBalance() != null && x.getBalance().compareTo(BigInteger.ZERO) > 0)
                      .map(tb -> (Callable<NonFungibleAsset>) () -> getNonFungibleAsset(tb.getTokenId().toString(), contract, tb.getMetadata()))
                      .collect(Collectors.toList());

    }

    protected NonFungibleContract createContract(Erc721TokenBalances token) {
        return NonFungibleContract.builder()
                                  .address(token.getAddress())
                                  .type(token.getType().name())
                                  .name(token.getName())
                                  .symbol(token.getSymbol())
                                  .build();
    }

    protected NonFungibleContract createContract(Erc1155TokenBalances token) {
        return NonFungibleContract.builder()
                                  .address(token.getAddress())
                                  .name(token.getName())
                                  .symbol(token.getSymbol())
                                  .type(token.getType().name())
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

    protected NonFungibleAsset getNonFungibleAsset(String tokenId,
                                                   NonFungibleContract contract,
                                                   String strMetaData) {
        if (StringUtils.isNotBlank(strMetaData)) {
            NonFungibleMetaData metaData = metadataParser.parseMetaData(getSecretType(), tokenId, contract.getType(), contract.getAddress(), strMetaData);
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
                                   .attributes(metaData.getAttributes())
                                   .build();
        }
        return NonFungibleAsset.builder()
                               .tokenId(tokenId)
                               .contract(contract)
                               .name(tokenId)
                               .description(contract.getAddress())
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
                                   .attributes(metaData.getAttributes())
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
        return azraelClient.getContract(contractAddress)
                           .map(token -> NonFungibleContract.builder()
                                                            .type(token.getContractType().name())
                                                            .address(contractAddress)
                                                            .name(token.getName())
                                                            .symbol(token.getSymbol())
                                                            .build())
                           .orElse(null);
    }

}
