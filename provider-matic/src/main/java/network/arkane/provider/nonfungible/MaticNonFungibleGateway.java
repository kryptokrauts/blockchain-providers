package network.arkane.provider.nonfungible;

import lombok.SneakyThrows;
import network.arkane.provider.balance.MaticBlockscoutDiscoveryService;
import network.arkane.provider.business.token.BusinessNonFungibleGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleContract;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaticNonFungibleGateway implements NonFungibleGateway {

    public static final String CONTRACT_TYPE = "ERC-721";
    private MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService;
    private BusinessNonFungibleGateway businessNonFungibleGateway;

    public MaticNonFungibleGateway(final MaticBlockscoutDiscoveryService maticBlockscoutDiscoveryService,
                                   BusinessNonFungibleGateway businessNonFungibleGateway) {
        this.maticBlockscoutDiscoveryService = maticBlockscoutDiscoveryService;
        this.businessNonFungibleGateway = businessNonFungibleGateway;
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }

    @Override
    @SneakyThrows
    public List<NonFungibleAsset> listNonFungibles(final String walletAddress,
                                                   final String... contractAddresses) {
        //        return maticBlockscoutDiscoveryService.getTokens(walletAddress)
        //                                              .stream()
        //                                              .filter(token -> token.getType().equals(CONTRACT_TYPE))
        //                                              .map(token -> {
        //                                                  return NonFungibleAsset.builder()
        //                                                                         .contract(NonFungibleContract.builder().build())
        //                                                                         .name(token.getName())
        //                                                                         .owner(walletAddress)
        //                                                                         .tokenId("TODO")
        //                                                                         .build();
        //                                              }).collect(Collectors.toList());
        return businessNonFungibleGateway.listNonFungibles(SecretType.MATIC, walletAddress, contractAddresses);
    }

    @Override
    public NonFungibleAsset getNonFungible(final String contractAddress,
                                           final String tokenId) {
        return businessNonFungibleGateway.getNonFungible(SecretType.MATIC, contractAddress, tokenId);
    }

    @Override
    public NonFungibleContract getNonFungibleContract(final String contractAddress) {
        return businessNonFungibleGateway.getNonFungibleContract(SecretType.MATIC, contractAddress);
    }
}
