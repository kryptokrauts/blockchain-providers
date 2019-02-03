package network.arkane.provider.litecoin.unspent;

import network.arkane.provider.blockcypher.BlockcypherGateway;
import network.arkane.provider.blockcypher.Network;
import network.arkane.provider.blockcypher.domain.BlockcypherAddressUnspents;
import network.arkane.provider.blockcypher.domain.BlockcypherTransactionRef;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UnspentLitecoinService {
    private final BlockcypherGateway blockcypherGateway;

    public UnspentLitecoinService(BlockcypherGateway blockcypherGateway) {
        this.blockcypherGateway = blockcypherGateway;
    }

    public List<Unspent> getUnspentForAddress(final String address) {
        BlockcypherAddressUnspents unspentTransactions = blockcypherGateway.getUnspentTransactions(Network.LITECOIN, address);

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
