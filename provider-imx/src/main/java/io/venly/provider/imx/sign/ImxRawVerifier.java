package io.venly.provider.imx.sign;

import lombok.extern.slf4j.Slf4j;
import network.arkane.provider.sign.BlockchainProviderRawVerifiable;
import network.arkane.provider.sign.ImxRawVerifiable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImxRawVerifier extends BlockchainProviderRawVerifiable<ImxRawVerifiable> {

    @Override
    public boolean isValidSignature(final ImxRawVerifiable verifiable) {
        //todo implement IMX verifier
        return true;
    }
}
