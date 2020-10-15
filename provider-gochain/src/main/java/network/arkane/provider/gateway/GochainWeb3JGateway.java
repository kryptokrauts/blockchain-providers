package network.arkane.provider.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;

import java.util.Optional;


@Component
@Slf4j
public class GochainWeb3JGateway extends EvmWeb3jGateway {
    private final EnsResolver ensResolver;

    public GochainWeb3JGateway(@Qualifier("gochainWeb3j") Web3j gochainWeb3j,
                               final @Value("${network.arkane.gochain.deltabalances.contract-address}") String deltaBalancesAddress) {
        super(gochainWeb3j, deltaBalancesAddress);
        ensResolver = new EnsResolver(gochainWeb3j);
    }


    @Cacheable(value = "address_to_ens_gochain", key = "#address")
    public Optional<String> getEnsName(String address) {
        try {
            return Optional.ofNullable(ensResolver.reverseResolve(address));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Cacheable(value = "ens_to_address_gochain", key = "#ensName")
    public Optional<String> getAddressForEnsName(String ensName) {
        try {
            return Optional.ofNullable(ensResolver.resolve(ensName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
