package network.arkane.provider.hedera.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.hedera.HederaClientFactory;
import network.arkane.provider.hedera.secret.generation.HederaSecretKey;
import network.arkane.provider.hedera.sign.handler.HbarTransferHandler;
import network.arkane.provider.sign.Signer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HbarSingleTransferSigner extends HederaSingleTransferSigner<HbarTransferSignable> implements Signer<HbarTransferSignable, HederaSecretKey> {

    public HbarSingleTransferSigner(final HederaClientFactory clientFactory,
                                    final HbarTransferHandler hbarTransferHandler) {
        super(clientFactory, hbarTransferHandler);
    }
}
