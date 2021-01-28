package network.arkane.provider.inventory;

import network.arkane.blockchainproviders.blockscout.dto.BlockscoutTokenBalance;
import network.arkane.provider.business.token.model.TokenDto;
import network.arkane.provider.nonfungible.EvmNonFungibleGateway;
import network.arkane.provider.nonfungible.domain.NonFungibleAsset;
import network.arkane.provider.nonfungible.domain.NonFungibleAssetBalance;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EvmInventoryService implements InventoryService {

    private EvmNonFungibleGateway nonFungibleGateway;

    public EvmInventoryService(EvmNonFungibleGateway nonFungibleGateway) {
        this.nonFungibleGateway = nonFungibleGateway;
    }

    public Inventory getInventory(final String walletAddress,
                                  final String... contractAddresses) {
        List<NonFungibleAssetBalance> tokens = nonFungibleGateway.listNonFungibles(walletAddress, contractAddresses);

        Map<String, Map<String, TokenTypeInventory>> tokenTypesByContract = groupTokens(tokens);

        return createInventory(tokenTypesByContract);

    }

    @NotNull
    private Map<String, Map<String, TokenTypeInventory>> groupTokens(List<NonFungibleAssetBalance> tokens) {
        Map<String, Map<String, TokenTypeInventory>> tokenTypesByContract = new HashMap<>();
        tokens
                .forEach(tokenBalance -> {
                    NonFungibleAsset token = tokenBalance.getNonFungibleAsset();
                    if (token != null && token.getContract().getType() != null && token.getId() != null) {
                        tokenTypesByContract.computeIfAbsent(token.getContract().getAddress(), s -> new HashMap<>());
                        Map<String, TokenTypeInventory> byType = tokenTypesByContract.get(token.getContract().getAddress());
                        String tokenTypeId = token.getId();
                        if (!byType.containsKey(tokenTypeId)) {
                            List<String> tokenIds = new ArrayList<>();
                            tokenIds.add(token.getId());
                            byType.put(tokenTypeId, TokenTypeInventory.builder()
                                                                      .fungible(token.getFungible())
                                                                      .balance(BigInteger.ONE)
                                                                      .tokenTypeId(tokenTypeId)
                                                                      .tokenIds(tokenIds)
                                                                      .build());
                        } else {
                            TokenTypeInventory tokenTypeInventory = byType.get(tokenTypeId);
                            tokenTypeInventory.setBalance(tokenTypeInventory.getBalance().add(tokenBalance.getBalance()));
                            tokenTypeInventory.getTokenIds().add(token.getId());
                        }
                    }
                });
        return tokenTypesByContract;
    }

    private Long getRealBalance(BlockscoutTokenBalance token,
                                TokenDto tokenDto) {
        return tokenDto.getTokenType().isNf()
               ? token.getBalanceAsLong()
               : token.getBalance().multiply(BigInteger.TEN.pow(tokenDto.getTokenType().getDecimals())).longValue();
    }

    private Inventory createInventory(Map<String, Map<String, TokenTypeInventory>> tokenTypesByContract) {
        Inventory inventory = Inventory.builder().contracts(new ArrayList<>()).build();
        tokenTypesByContract.forEach((contractAddress, byTypes) -> {
            ContractInventory contractInventory = ContractInventory.builder().contractAddress(contractAddress).tokenTypes(new ArrayList<>()).build();
            inventory.getContracts().add(contractInventory);
            byTypes.forEach((tokenTypeId, typeInventory) -> {
                contractInventory.getTokenTypes().add(typeInventory);
            });
        });
        return inventory;
    }


}
