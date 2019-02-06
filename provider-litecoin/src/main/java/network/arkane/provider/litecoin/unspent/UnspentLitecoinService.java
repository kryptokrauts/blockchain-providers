package network.arkane.provider.litecoin.unspent;

import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import network.arkane.provider.blockcypher.domain.BlockcypherTransactionRef;
import network.arkane.provider.litecoin.LitecoinEnv;
import network.arkane.provider.litecoin.address.LitecoinP2SHConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UnspentLitecoinService {
    private final LitecoinEnv litecoinEnv;
    private final BlockcypherGateway blockcypherGateway;
    private final LitecoinP2SHConverter litecoinP2SHConverter;

    public UnspentLitecoinService(LitecoinEnv litecoinEnv,
                                  BlockcypherGateway blockcypherGateway,
                                  LitecoinP2SHConverter litecoinP2SHConverter) {
        this.litecoinEnv = litecoinEnv;
        this.blockcypherGateway = blockcypherGateway;
        this.litecoinP2SHConverter = litecoinP2SHConverter;
    }

    public List<Unspent> getUnspentForAddress(final String address) {
        BlockcypherAddressUnspents unspentTransactions = blockcypherGateway.getUnspentTransactions(
                litecoinEnv.getNetwork(),
                litecoinP2SHConverter.convert(address)
        );

        if (unspentTransactions == null || unspentTransactions.getTransactionRefs() == null || unspentTransactions.getTransactionRefs().size() == 0) {
            return new ArrayList<>();
        }

        return unspentTransactions
                .getTransactionRefs()
                .stream()
                .map(this::toUnspent)
                .collect(Collectors.toList());
    }

    private Unspent toUnspent(BlockcypherTransactionRef transactionRef) {
        return Unspent.builder()
                .amount(transactionRef.getValue().longValue())
                .txId(transactionRef.getTransactionHash())
                .vOut(transactionRef.getTransactionOutputN().longValue())
                .scriptPubKey(transactionRef.getScript())
                .build();
    }
}
