package network.arkane.provider.hedera.nonfungible;

import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.TokenId;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.exceptions.ArkaneException;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.balance.HederaTokenInfoService;
import network.arkane.provider.hedera.mirror.MirrorNodeClient;
import network.arkane.provider.hedera.mirror.dto.MirrorNodeNft;
import network.arkane.provider.hedera.mirror.dto.NftWalletDto;
import network.arkane.provider.nonfungible.NonFungibleGateway;
import network.arkane.provider.nonfungible.NonFungibleMetaData;
import network.arkane.provider.nonfungible.domain.Attribute;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import network.arkane.provider.token.TokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HederaNonfungibleGateway implements NonFungibleGateway {

    public static final String NON_FUNGIBLE_UNIQUE = "NON_FUNGIBLE_UNIQUE";
    private final Client hederaClient;
    private final HederaTokenInfoService tokenInfoService;
    private final MirrorNodeClient mirrorNodeClient;
    private final HederaMetaDataParser metaDataParser;
    private final HederaNftContractInfoService hederaNftContractInfoService;

    public HederaNonfungibleGateway(final HederaClientFactory clientFactory,
                                    final HederaTokenInfoService tokenInfoService,
                                    final MirrorNodeClient mirrorNodeClient,
                                    final Optional<CacheManager> cacheManager,
                                    final HederaNftContractInfoService hederaNftContractInfoService) {
        this.hederaClient = clientFactory.getClientWithOperator();
        this.tokenInfoService = tokenInfoService;
        this.mirrorNodeClient = mirrorNodeClient;
        metaDataParser = new HederaMetaDataParser(cacheManager);

        this.hederaNftContractInfoService = hederaNftContractInfoService;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.HEDERA;
    }

    @Override
    public List<NonFungibleAssetBalance> listNonFungibles(String walletId,
                                                          String... contractAddresses) {
        if (contractAddresses == null || contractAddresses.length == 0) {
            return listNftsForAddress(walletId);
        } else {
            Set<String> contracts = Arrays.stream(contractAddresses).map(String::toLowerCase).collect(Collectors.toSet());
            return listNftsForAddress(walletId)
                    .stream()
                    .filter(nft -> contracts.contains(nft.getContract().getAddress().toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public NonFungibleAsset getNonFungible(String contractAddress,
                                           String tokenId) {
        return mirrorNodeClient.getNft(contractAddress, tokenId)
                               .map(this::getNonFungibleAsset)
                               .orElse(null);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(String contractAddress) {
        return hederaNftContractInfoService.getContractInfo(contractAddress).orElse(null);
    }

    public List<NftWalletDto> getNftWallets(String walletAddress, String spender) {
        return mirrorNodeClient.getNftWallets(walletAddress, spender);
    }

    private List<NonFungibleAssetBalance> listNftsForAddress(String address) {
        Map<TokenId, Long> tokenBalances = getTokenBalancesFromChain(address);
        return tokenBalances.entrySet().stream()
                            .flatMap(e -> {
                                Optional<TokenInfo> tokenInfo = tokenInfoService.getTokenInfo(e.getKey().toString());
                                if (tokenInfo.isEmpty() || !NON_FUNGIBLE_UNIQUE.equalsIgnoreCase(tokenInfo.get().getType())) return Stream.empty();
                                return mirrorNodeClient.getNfts(e.getKey().toString(), address)
                                                       .stream()
                                                       .filter(nft -> StringUtils.isNotBlank(nft.getMetadata()))
                                                       .map(this::getNonFungibleAsset)
                                                       .filter(Objects::nonNull)
                                                       .map(nfa -> NonFungibleAssetBalance.from(nfa, BigInteger.ONE, BigInteger.ONE));
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

    }

    private NonFungibleAsset getNonFungibleAsset(MirrorNodeNft nft) {
        if (StringUtils.isBlank(nft.getMetadata())) return null;
        NonFungibleMetaData nonFungibleMetaData = metaDataParser.parseMetaData(nft.getTokenId(), nft.getSerialNumber(), nft.getMetadata());
        if (nonFungibleMetaData == null) return null;
        String imageUrl = nonFungibleMetaData.getImage().map(i -> i.replaceAll(" ", "%20")).orElse(null);
        List<Attribute> enrichedAttributes = nonFungibleMetaData.getEnrichedAttributes();
        if (nonFungibleMetaData.getProperty("creator") != null) {
            enrichedAttributes = new ArrayList<>(enrichedAttributes);
            enrichedAttributes.add(Attribute.builder()
                                            .type("property")
                                            .name("Creator")
                                            .value(nonFungibleMetaData.getProperty("creator"))
                                            .build());
        }
        return NonFungibleAsset.builder()
                               .name(nonFungibleMetaData.getName())
                               .imageUrl(imageUrl)
                               .imagePreviewUrl(imageUrl)
                               .imageThumbnailUrl(imageUrl)
                               .id(nft.getSerialNumber().toString())
                               .contract(getNonFungibleContract(nft.getTokenId()))
                               .description(nonFungibleMetaData.getDescription())
                               .url(nonFungibleMetaData.getExternalUrl().orElse(null))
                               .animationUrl(nonFungibleMetaData.getAnimationUrl().orElse(null))
                               .animationUrls(nonFungibleMetaData.getAnimationUrls())
                               .attributes(enrichedAttributes)
                               .build();
    }

    private Map<TokenId, Long> getTokenBalancesFromChain(String address) {
        try {
            return new AccountBalanceQuery()
                    .setAccountId(AccountId.fromString(address))
                    .execute(hederaClient)
                    .tokens;
        } catch (TimeoutException | PrecheckStatusException e) {
            throw ArkaneException.arkaneException()
                                 .cause(e)
                                 .message(e.getMessage())
                                 .errorCode("hedera.balance.error")
                                 .build();
        }
    }

}
