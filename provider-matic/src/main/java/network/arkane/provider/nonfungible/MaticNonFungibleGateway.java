package network.arkane.provider.nonfungible;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.chain.SecretType;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class MaticNonFungibleGateway extends EvmNonFungibleGateway {

    public MaticNonFungibleGateway(List<EvmNonFungibleStrategy> evmNonFungibleStrategies) {
        super(evmNonFungibleStrategies);
    }

    @Override
    public SecretType getSecretType() {
        return SecretType.MATIC;
    }


}
