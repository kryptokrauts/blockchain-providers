package network.arkane.provider.gateway;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.web3j.EvmWeb3jGateway;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;

import java.util.Optional;


@Slf4j
public class EthereumWeb3JGateway extends EvmWeb3jGateway {

    private final EnsResolver ensResolver;

    @Override
    public SecretType getSecretType() {
        return SecretType.ETHEREUM;
    }

    public EthereumWeb3JGateway(final Web3j ethereumWeb3j,
                                final String deltaBalancesAddress) {
        super(ethereumWeb3j, deltaBalancesAddress);
        if (StringUtils.isEmpty(deltaBalancesAddress)) {
            throw new IllegalArgumentException("Address for deltabalances should be set [ethereum]");
        }
        ensResolver = new EnsResolver(ethereumWeb3j);
    }

    @Cacheable(value = "address_to_ens", key = "#address")
    public Optional<String> getEnsName(String address) {
        try {
            return Optional.ofNullable(ensResolver.reverseResolve(address));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Cacheable(value = "ens_to_address", key = "#ensName")
    public Optional<String> getAddressForEnsName(String ensName) {
        try {
            return Optional.ofNullable(ensResolver.resolve(ensName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
