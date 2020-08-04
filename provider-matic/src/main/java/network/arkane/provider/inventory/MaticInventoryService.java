package network.arkane.provider.inventory;

import lombok.AllArgsConstructor;
import network.arkane.blockchainproviders.blockscout.BlockscoutClient;
import network.arkane.blockchainproviders.blockscout.dto.BlockscoutTokenBalance;
import network.arkane.blockchainproviders.blockscout.dto.ERC1155BlockscoutToken;
import network.arkane.provider.business.token.BusinessTokenGateway;
import network.arkane.provider.business.token.model.TokenDto;
import network.arkane.provider.chain.SecretType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MaticInventoryService implements InventoryService {

    private final BlockscoutClient maticBlockscoutClient;
    private final BusinessTokenGateway businessTokenGateway;

    public Inventory getInventory(final String walletAddress,
                                  final String... contractAddresses) {
        Set<String> contracts = contractAddresses == null ? new HashSet<>() : Arrays.stream(contractAddresses).map(String::toLowerCase).collect(Collectors.toSet());

        List<ERC1155BlockscoutToken> blockscoutTokens = getTokensFromBlockscout(walletAddress, contracts, contractAddresses);

        Map<String, Map<Long, TokenTypeInventory>> tokenTypesByContract = groupTokens(blockscoutTokens);

        return createInventory(tokenTypesByContract);

    }

    @NotNull
    private Map<String, Map<Long, TokenTypeInventory>> groupTokens(List<ERC1155BlockscoutToken> blockscoutTokens) {
        Map<String, Map<Long, TokenTypeInventory>> tokenTypesByContract = new HashMap<>();
        blockscoutTokens
                .forEach(t -> {
                    t.getTokens().forEach(token -> {
                        if (token.getBalanceAsLong() > 0) {
                            TokenDto tokenDto = businessTokenGateway.getToken(t.getContractAddress(), token.getTokenId());
                            if (tokenDto != null && tokenDto.getTokenType() != null && tokenDto.getTokenType().getId() != null) {
                                tokenTypesByContract.computeIfAbsent(t.getContractAddress(), s -> new HashMap<>());
                                Map<Long, TokenTypeInventory> byType = tokenTypesByContract.get(t.getContractAddress());
                                Long tokenTypeId = tokenDto.getTokenType().getId();
                                if (!byType.containsKey(tokenTypeId)) {
                                    List<String> tokenIds = new ArrayList<>();
                                    tokenIds.add(token.getTokenId().toString());
                                    byType.put(tokenTypeId, TokenTypeInventory.builder()
                                                                              .fungible(token.getFungible())
                                                                              .balance(getRealBalance(token, tokenDto))
                                                                              .tokenTypeId(tokenTypeId)
                                                                              .tokenIds(tokenIds)
                                                                              .build());
                                } else {
                                    TokenTypeInventory tokenTypeInventory = byType.get(tokenTypeId);
                                    tokenTypeInventory.setBalance(tokenTypeInventory.getBalance() + getRealBalance(token, tokenDto));
                                    tokenTypeInventory.getTokenIds().add(token.getTokenId().toString());
                                }
                            }
                        }
                    });
                });
        return tokenTypesByContract;
    }

    private Long getRealBalance(BlockscoutTokenBalance token,
                                TokenDto tokenDto) {
        return tokenDto.getTokenType().isNf()
               ? token.getBalanceAsLong()
               : token.getBalance().multiply(BigInteger.TEN.pow(tokenDto.getTokenType().getDecimals())).longValue();
    }

    private Inventory createInventory(Map<String, Map<Long, TokenTypeInventory>> tokenTypesByContract) {
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

    @NotNull
    private List<ERC1155BlockscoutToken> getTokensFromBlockscout(String walletAddress,
                                                                 Set<String> contracts,
                                                                 String[] contractAddresses) {
        return maticBlockscoutClient.getTokenBalances(walletAddress)
                                    .stream()
                                    .filter(t -> t.getType().endsWith("1155"))
                                    .filter(x -> contractAddresses == null
                                                 || contractAddresses.length == 0
                                                 || contracts.contains(x.getContractAddress().toLowerCase()))
                                    .map(t -> (ERC1155BlockscoutToken) t)
                                    .collect(Collectors.toList());
    }


    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }
}
