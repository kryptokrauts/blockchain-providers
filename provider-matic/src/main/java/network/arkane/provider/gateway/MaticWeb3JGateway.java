package network.arkane.provider.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;

import java.util.Optional;


@Slf4j
public class MaticWeb3JGateway extends EvmWeb3jGateway {

    private final EnsResolver ensResolver;

    public MaticWeb3JGateway(final @Qualifier("maticWeb3j") Web3j maticWeb3j,
                             final String deltaBalancesAddress) {
        super(maticWeb3j, deltaBalancesAddress);
        ensResolver = new EnsResolver(maticWeb3j);
    }

    @Cacheable(value = "address_to_ens", key = "'matic-' + #address")
    public Optional<String> getEnsName(String address) {
        try {
            return Optional.ofNullable(ensResolver.reverseResolve(address));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Cacheable(value = "ens_to_address", key = "'matic' + #ensName")
    public Optional<String> getAddressForEnsName(String ensName) {
        try {
            return Optional.ofNullable(ensResolver.resolve(ensName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
