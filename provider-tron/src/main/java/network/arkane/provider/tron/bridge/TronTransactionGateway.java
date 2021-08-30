package network.arkane.provider.tron.bridge;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.bridge.TransactionGateway;
import network.arkane.provider.chain.SecretType;
import network.arkane.provider.sign.domain.Signature;
import network.arkane.provider.sign.domain.TransactionSignature;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class TronTransactionGateway implements TransactionGateway {

    @Override
    @SneakyThrows
    public Signature submit(final TransactionSignature transactionSignature,
                            final Optional<String> endpoint) {
        throw new RuntimeException("Tron has been deprecated");
    }


    @Override
    public SecretType getType() {
        return SecretType.TRON;
    }
}
