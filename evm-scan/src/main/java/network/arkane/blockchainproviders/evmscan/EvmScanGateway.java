package network.arkane.blockchainproviders.evmscan;

import network.arkane.blockchainproviders.evmscan.dto.EvmAccount;
import network.arkane.provider.chain.SecretType;

public abstract class EvmScanGateway {
    private final EvmScanClient evmScanClient;

    protected EvmScanGateway(EvmScanClient evmScanClient) {
        this.evmScanClient = evmScanClient;
    }

    public abstract SecretType secretType();

    public EvmAccount getTransactions(final String address,
                                      final Long page,
                                      final Long offset) {
        return EvmAccount.builder()
                         .transactions(evmScanClient.getTransactionList(address, page, offset)
                                                    .getResult())
                         .address(address)
                         .chain(secretType())
                         .build();
    }

    public EvmAccount getTransactionList(final String address) {
        return this.getTransactions(address, 0L, 0L);
    }
}
