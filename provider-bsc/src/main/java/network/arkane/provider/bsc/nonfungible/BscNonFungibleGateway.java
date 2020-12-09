package network.arkane.provider.bsc.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.nonfungible.EvmNonFungibleGateway;
import network.arkane.provider.nonfungible.EvmNonFungibleStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BscNonFungibleGateway extends EvmNonFungibleGateway {

    public BscNonFungibleGateway(List<EvmNonFungibleStrategy> evmNonFungibleStrategies) {
        super(evmNonFungibleStrategies);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.BSC;
    }


}
