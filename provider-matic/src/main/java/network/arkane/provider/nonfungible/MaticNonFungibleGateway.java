package network.arkane.provider.nonfungible;

import network.arkane.provider.balance.MaticBlockscoutDiscoveryService;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaticNonFungibleGateway implements NonFungibleGateway {

    public static final String CONTRACT_TYPE = "ERC-721";
    private MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService;

    public MaticNonFungibleGateway(final MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService) {
        this.maticBlockscoutDiscoveryService = maticBlockscoutDiscoveryService;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }

    @Override
    public List<NonFungibleAsset> listNonFungibles(final String walletAddress,
                                                   final String... contractAddresses) {
        return maticBlockscoutDiscoveryService.getTokens(walletAddress)
                                              .stream()
                                              .filter(token -> token.getType().equals(CONTRACT_TYPE))
                                              .map(token -> {
                                                  return NonFungibleAsset.builder()
                                                                         .contract(NonFungibleContract.builder().build())
                                                                         .name(token.getName())
                                                                         .owner(walletAddress)
                                                                         .tokenId("TODO")
                                                                         .build();
                                              }).collect(Collectors.toList());
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return null;
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return NonFungibleContract.builder()
                                  .address(contractAddress)
                                  .type(CONTRACT_TYPE)
                                  .build();
    }
}
